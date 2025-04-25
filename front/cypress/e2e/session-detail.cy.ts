describe('Session Detail spec', () => {
  const sessionData = {
    id: 1,
    name: 'Yoga session',
    description: 'Description détaillée de la session de yoga',
    date: '2025-04-25T10:00:00.000Z',
    teacher_id: 2,
    users: [3, 4],
    createdAt: '2025-04-20T10:00:00.000Z',
    updatedAt: '2025-04-23T14:30:00.000Z',
  };

  const teacherData = {
    id: 2,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john.doe@example.com',
  };

  describe('As admin user', () => {
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

      // Intercepte la requête pour récupérer les détails de la session
      cy.intercept('GET', '/api/session/1', {
        body: sessionData,
      }).as('getSessionDetail');

      // Intercepte la requête pour récupérer les détails de l'enseignant
      cy.intercept('GET', '/api/user/2', {
        body: teacherData,
      }).as('getTeacherDetail');

      cy.visit('/sessions/detail/1');
      cy.wait(['@getSessionDetail', '@getTeacherDetail']);
    });

    it('Should display session details correctly', () => {
      // Vérifie le titre de la session
      cy.contains('h1', 'Yoga session').should('be.visible');

      // Vérifie les informations de l'enseignant
      cy.contains('span', 'John DOE').should('be.visible');

      // Vérifie le nombre de participants
      cy.contains('span', '2 attendees').should('be.visible');

      // Vérifie la date de la session
      cy.get('.my2').contains('April 25, 2025').should('be.visible');

      // Vérifie la description
      cy.get('.description')
        .contains('Description détaillée de la session de yoga')
        .should('be.visible');

      // Vérifie la date de création et de mise à jour
      cy.get('.created')
        .contains(/Create at/)
        .should('be.visible');
      cy.get('.updated')
        .contains(/Last update/)
        .should('be.visible');
    });

    it('Should navigate back to sessions list when back button is clicked', () => {
      // Intercepte les requêtes pour la liste des sessions
      cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');

      // Clique sur le bouton de retour
      cy.get('button mat-icon').contains('arrow_back').click();

      // Vérifie la navigation vers la liste des sessions
      cy.url().should('include', '/sessions');
      cy.wait('@getSessions');
    });

    it('Should display delete button for admin users and handle deletion', () => {
      // Vérifie que le bouton de suppression est présent pour les admin
      cy.get('button[color="warn"]').contains('Delete').should('be.visible');

      // Intercepte la requête de suppression
      cy.intercept('DELETE', '/api/session/1', {
        statusCode: 200,
      }).as('deleteSession');

      // Intercepte les requêtes pour la liste des sessions (après suppression)
      cy.intercept('GET', '/api/session', { body: [] }).as('getSessions');

      // Clique sur le bouton de suppression
      cy.get('button[color="warn"]').contains('Delete').click();

      // Vérifie que la requête de suppression a été envoyée
      cy.wait('@deleteSession');

      // Vérifie le retour à la page de liste des sessions
      cy.url().should('include', '/sessions');
      cy.wait('@getSessions');
    });
  });

  describe('As regular user', () => {
    beforeEach(() => {
      // Simule un utilisateur connecté non-admin avec ID 3 (participant)
      cy.window().then((window) => {
        window.localStorage.setItem(
          'session',
          JSON.stringify({
            token: 'fake-token',
            type: 'Bearer',
            id: 3,
            username: 'user@studio.com',
            firstName: 'Regular',
            lastName: 'User',
            admin: false,
          })
        );
      });

      // Intercepte la requête pour récupérer les détails de la session
      cy.intercept('GET', '/api/session/1', {
        body: sessionData,
      }).as('getSessionDetail');

      // Intercepte la requête pour récupérer les détails de l'enseignant
      cy.intercept('GET', '/api/user/2', {
        body: teacherData,
      }).as('getTeacherDetail');

      cy.visit('/sessions/detail/1');
      cy.wait(['@getSessionDetail', '@getTeacherDetail']);
    });

    it('Should not display delete button for regular users', () => {
      // Vérifie que le bouton de suppression n'est pas présent
      cy.get('button').contains('Delete').should('not.exist');
    });

    it('Should display "Do not participate" button for already participating users', () => {
      // L'utilisateur courant a l'ID 3 et figure déjà dans les participants
      cy.contains('button', 'Do not participate').should('be.visible');
      cy.contains('button', 'Participate').should('not.exist');

      // Intercepte la requête pour se désinscrire
      cy.intercept('DELETE', '/api/session/1/participant/3', {
        statusCode: 200,
      }).as('unParticipate');

      // Clique sur le bouton pour ne plus participer
      cy.contains('button', 'Do not participate').click();

      // Vérifie que la requête a été envoyée
      cy.wait('@unParticipate');

      // Recharge la page avec mise à jour des participants (sans l'utilisateur 3)
      cy.intercept('GET', '/api/session/1', {
        body: {
          ...sessionData,
          users: [4], // L'utilisateur 3 a été retiré
        },
      }).as('getUpdatedSession');

      cy.wait('@getUpdatedSession');

      // Vérifie que le bouton Participate est maintenant affiché
      cy.contains('button', 'Participate').should('be.visible');
      cy.contains('button', 'Do not participate').should('not.exist');
    });

    it('Should display "Participate" button for non-participating users and handle participation', () => {
      // Simule un utilisateur connecté non-admin avec ID 5 (non participant)
      cy.window().then((window) => {
        window.localStorage.setItem(
          'session',
          JSON.stringify({
            token: 'fake-token',
            type: 'Bearer',
            id: 5,
            username: 'other@studio.com',
            firstName: 'Other',
            lastName: 'User',
            admin: false,
          })
        );
      });

      // Recharge la page
      cy.reload();
      cy.wait(['@getSessionDetail', '@getTeacherDetail']);

      cy.contains('button', 'Participate').should('be.visible');
      cy.contains('button', 'Do not participate').should('not.exist');

      // Intercepte la requête pour s'inscrire
      cy.intercept('POST', '/api/session/1/participant/5', {
        statusCode: 200,
      }).as('participate');

      // Clique sur le bouton pour participer
      cy.contains('button', 'Participate').click();

      // Vérifie que la requête a été envoyée
      cy.wait('@participate');

      // Recharge la page avec mise à jour des participants (avec l'utilisateur 5)
      cy.intercept('GET', '/api/session/1', {
        body: {
          ...sessionData,
          users: [...sessionData.users, 5], // L'utilisateur 5 a été ajouté
        },
      }).as('getUpdatedSession');

      cy.wait('@getUpdatedSession');

      // Vérifie que le bouton "Do not participate" est maintenant affiché
      cy.contains('button', 'Do not participate').should('be.visible');
      cy.contains('button', 'Participate').should('not.exist');
    });
  });
});
