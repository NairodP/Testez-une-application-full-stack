describe('Session Form spec', () => {
  // Données d'exemple pour un formulaire de session
  const sessionData = {
    id: 1,
    name: 'Yoga session',
    description: 'Description de la session de yoga',
    date: '2025-04-25',
    time: '10:00',
    teacher_id: 2,
    users: [],
    createdAt: '2025-04-20T10:00:00.000Z',
    updatedAt: '2025-04-23T14:30:00.000Z',
  };

  // Données pour les enseignants
  const teachersData = [
    {
      id: 2,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: true,
    },
    {
      id: 3,
      firstName: 'Jane',
      lastName: 'Smith',
      email: 'jane.smith@example.com',
      admin: false,
    },
  ];

  describe('Create new session', () => {
    beforeEach(() => {
      // Simule un utilisateur connecté en tant qu'admin
      cy.window().then((window) => {
        window.localStorage.setItem(
          'session',
          JSON.stringify({
            token: 'fake-token',
            type: 'Bearer',
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'Admin',
            lastName: 'User',
            admin: true,
          })
        );
      });

      // Intercepte la requête pour récupérer la liste des enseignants
      cy.intercept('GET', '/api/user', {
        body: teachersData,
      }).as('getTeachers');

      // Navigation vers le formulaire de création de session
      cy.visit('/sessions/create');
      cy.wait('@getTeachers');
    });

    it('Should display the create form with empty fields', () => {
      // Vérifier que le titre du formulaire est correct
      cy.contains('h1', 'Create Session').should('be.visible');

      // Vérifier que les champs sont vides
      cy.get('input[formControlName="name"]').should('have.value', '');
      cy.get('textarea[formControlName="description"]').should(
        'have.value',
        ''
      );
      cy.get('input[formControlName="date"]').should('have.value', '');
      cy.get('input[formControlName="time"]').should('have.value', '');

      // Vérifier que le sélecteur d'enseignant est présent
      cy.get('mat-select[formControlName="teacher_id"]').should('exist');

      // Vérifier que les boutons sont présents
      cy.contains('button', 'Cancel').should('be.visible');
      cy.contains('button', 'Save').should('be.visible');
    });

    it('Should show validation errors for empty fields', () => {
      // Cliquer sur le bouton Save sans remplir les champs
      cy.contains('button', 'Save').click();

      // Vérifier que les messages d'erreur sont affichés
      cy.contains('Name is required').should('be.visible');
      cy.contains('Description is required').should('be.visible');
      cy.contains('Date is required').should('be.visible');
      cy.contains('Time is required').should('be.visible');
      cy.contains('Teacher is required').should('be.visible');
    });

    it('Should create a new session when form is submitted with valid data', () => {
      // Intercepte la requête de création de session
      cy.intercept('POST', '/api/session', {
        statusCode: 201,
        body: sessionData,
      }).as('createSession');

      // Intercepte la requête de récupération des sessions (après création)
      cy.intercept('GET', '/api/session', { body: [sessionData] }).as(
        'getSessions'
      );

      // Remplir le formulaire
      cy.get('input[formControlName="name"]').type('Yoga session');
      cy.get('textarea[formControlName="description"]').type(
        'Description de la session de yoga'
      );
      cy.get('input[formControlName="date"]').type('2025-04-25');
      cy.get('input[formControlName="time"]').type('10:00');

      // Sélectionner un enseignant
      cy.get('mat-select[formControlName="teacher_id"]').click();
      cy.get('mat-option').contains('John DOE').click();

      // Soumettre le formulaire
      cy.contains('button', 'Save').click();

      // Vérifier que la requête de création a été envoyée
      cy.wait('@createSession');

      // Vérifier la redirection vers la liste des sessions
      cy.url().should('include', '/sessions');
    });

    it('Should navigate back to sessions list when cancel button is clicked', () => {
      // Intercepte la requête de récupération des sessions (après annulation)
      cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');

      // Cliquer sur le bouton Cancel
      cy.contains('button', 'Cancel').click();

      // Vérifier la redirection vers la liste des sessions
      cy.url().should('include', '/sessions');
      cy.wait('@getSessions');
    });
  });

  describe('Edit existing session', () => {
    beforeEach(() => {
      // Simule un utilisateur connecté en tant qu'admin
      cy.window().then((window) => {
        window.localStorage.setItem(
          'session',
          JSON.stringify({
            token: 'fake-token',
            type: 'Bearer',
            id: 1,
            username: 'yoga@studio.com',
            firstName: 'Admin',
            lastName: 'User',
            admin: true,
          })
        );
      });

      // Intercepte la requête pour récupérer la session à modifier
      cy.intercept('GET', '/api/session/1', {
        body: sessionData,
      }).as('getSession');

      // Intercepte la requête pour récupérer la liste des enseignants
      cy.intercept('GET', '/api/user', {
        body: teachersData,
      }).as('getTeachers');

      // Navigation vers le formulaire d'édition
      cy.visit('/sessions/update/1');
      cy.wait(['@getSession', '@getTeachers']);
    });

    it('Should display the edit form with pre-filled fields', () => {
      // Vérifier que le titre du formulaire est correct
      cy.contains('h1', 'Update Session').should('be.visible');

      // Vérifier que les champs sont pré-remplis avec les données de la session
      cy.get('input[formControlName="name"]').should(
        'have.value',
        'Yoga session'
      );
      cy.get('textarea[formControlName="description"]').should(
        'have.value',
        'Description de la session de yoga'
      );

      // La date et l'heure sont formatées différemment dans le formulaire
      cy.get('input[formControlName="date"]').should('not.have.value', '');
      cy.get('input[formControlName="time"]').should('not.have.value', '');

      // Vérifier que les boutons sont présents
      cy.contains('button', 'Cancel').should('be.visible');
      cy.contains('button', 'Save').should('be.visible');
    });

    it('Should update the session when form is submitted with valid data', () => {
      // Intercepte la requête de mise à jour
      cy.intercept('PUT', '/api/session/1', {
        statusCode: 200,
        body: {
          ...sessionData,
          name: 'Updated Yoga session',
          description: 'Description mise à jour',
        },
      }).as('updateSession');

      // Intercepte la requête de récupération des sessions (après mise à jour)
      cy.intercept('GET', '/api/session', {
        body: [
          {
            ...sessionData,
            name: 'Updated Yoga session',
            description: 'Description mise à jour',
          },
        ],
      }).as('getSessions');

      // Modifier les champs du formulaire
      cy.get('input[formControlName="name"]')
        .clear()
        .type('Updated Yoga session');
      cy.get('textarea[formControlName="description"]')
        .clear()
        .type('Description mise à jour');

      // Soumettre le formulaire
      cy.contains('button', 'Save').click();

      // Vérifier que la requête de mise à jour a été envoyée
      cy.wait('@updateSession');

      // Vérifier la redirection vers la liste des sessions
      cy.url().should('include', '/sessions');
    });
  });
});
