describe('Session List spec', () => {
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

    // Intercepte la requête pour récupérer les sessions
    cy.intercept('GET', '/api/session', {
      body: [
        {
          id: 1,
          name: 'Session 1',
          description: 'Description de la session 1',
          date: '2025-04-25T10:00:00.000Z',
          teacher_id: 1,
          users: [],
          createdAt: '2025-04-20T10:00:00.000Z',
          updatedAt: '2025-04-20T10:00:00.000Z',
        },
        {
          id: 2,
          name: 'Session 2',
          description: 'Description de la session 2',
          date: '2025-04-26T10:00:00.000Z',
          teacher_id: 2,
          users: [1, 3],
          createdAt: '2025-04-21T10:00:00.000Z',
          updatedAt: '2025-04-21T10:00:00.000Z',
        },
      ],
    }).as('getSessions');

    cy.visit('/sessions');
    cy.wait('@getSessions');
  });

  it('Should display list of sessions', () => {
    // Vérifie que le titre est affiché
    cy.contains('mat-card-title', 'Rentals available').should('be.visible');

    // Vérifie que les sessions sont affichées
    cy.get('mat-card.item').should('have.length', 2);

    // Vérifie les informations de la première session
    cy.get('mat-card.item')
      .first()
      .within(() => {
        cy.contains('mat-card-title', 'Session 1').should('be.visible');
        cy.contains('mat-card-subtitle', /Session on/).should('be.visible');
        cy.contains('p', 'Description de la session 1').should('be.visible');
      });
  });

  it('Should display create button for admin users', () => {
    // Vérifie que le bouton de création est présent pour les admin
    cy.get('button').contains('Create').should('be.visible');

    // Clique sur le bouton et vérifie la navigation
    cy.get('button').contains('Create').click();
    cy.url().should('include', '/sessions/create');
  });

  it('Should not display create button for non-admin users', () => {
    // Reconfigure pour un utilisateur non-admin
    cy.window().then((window) => {
      window.localStorage.setItem(
        'session',
        JSON.stringify({
          token: 'fake-token',
          type: 'Bearer',
          id: 2,
          username: 'user@studio.com',
          firstName: 'Regular',
          lastName: 'User',
          admin: false,
        })
      );
    });

    // Recharge la page
    cy.visit('/sessions');
    cy.wait('@getSessions');

    // Vérifie que le bouton de création n'est pas présent
    cy.contains('button', 'Create').should('not.exist');
  });

  it('Should navigate to session detail when clicking Detail button', () => {
    // Clique sur le bouton Detail de la première session
    cy.get('mat-card.item').first().contains('button', 'Detail').click();

    // Vérifie la navigation vers la page de détails
    cy.url().should('include', '/sessions/detail/1');
  });

  it('Should display edit button for admin users and navigate to update page', () => {
    // Vérifie que le bouton d'édition est présent pour les admin
    cy.get('mat-card.item')
      .first()
      .contains('button', 'Edit')
      .should('be.visible');

    // Clique sur le bouton et vérifie la navigation
    cy.get('mat-card.item').first().contains('button', 'Edit').click();
    cy.url().should('include', '/sessions/update/1');
  });

  it('Should not display edit button for non-admin users', () => {
    // Reconfigure pour un utilisateur non-admin
    cy.window().then((window) => {
      window.localStorage.setItem(
        'session',
        JSON.stringify({
          token: 'fake-token',
          type: 'Bearer',
          id: 2,
          username: 'user@studio.com',
          firstName: 'Regular',
          lastName: 'User',
          admin: false,
        })
      );
    });

    // Recharge la page
    cy.visit('/sessions');
    cy.wait('@getSessions');

    // Vérifie que le bouton d'édition n'est pas présent
    cy.contains('button', 'Edit').should('not.exist');
  });
});
