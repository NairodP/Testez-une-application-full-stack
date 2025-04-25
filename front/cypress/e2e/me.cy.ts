describe('Me Profile spec', () => {
  beforeEach(() => {
    // Simule un utilisateur connecté
    cy.window().then((window) => {
      window.localStorage.setItem(
        'session',
        JSON.stringify({
          token: 'fake-token',
          type: 'Bearer',
          id: 1,
          username: 'yoga@studio.com',
          firstName: 'Yoga',
          lastName: 'Master',
          admin: true,
        })
      );
    });

    // Intercepte la requête de récupération des données utilisateur
    cy.intercept('GET', '/api/user/1', {
      statusCode: 200,
      body: {
        id: 1,
        email: 'yoga@studio.com',
        lastName: 'Master',
        firstName: 'Yoga',
        admin: true,
        createdAt: '2023-01-01T00:00:00.000Z',
        updatedAt: '2023-01-01T00:00:00.000Z',
      },
    }).as('getUser');

    // Intercepte également la requête pour les sessions afin de vérifier la navigation
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [],
    }).as('getSessions');

    cy.visit('/me');
    cy.wait('@getUser');
  });

  it('Should display user information correctly', () => {
    // Vérifie que les informations utilisateur sont affichées correctement
    cy.contains('p', 'Name: Yoga MASTER').should('be.visible');
    cy.contains('p', 'Email: yoga@studio.com').should('be.visible');
    cy.contains('p', 'You are admin').should('be.visible');
    cy.contains('i', 'Create at:').should('be.visible');
    cy.contains('i', 'Last update:').should('be.visible');
  });

  it('Should navigate back when back button is clicked', () => {
    // Avant de naviguer, on définit la page précédente (généralement /sessions)
    cy.visit('/sessions');
    cy.wait('@getSessions');
    cy.visit('/me');
    cy.wait('@getUser');

    // Clique sur le bouton retour avec l'icône arrow_back
    cy.get('button').find('mat-icon').contains('arrow_back').click();

    // Vérifie que la navigation a ramené à /sessions
    cy.url().should('include', '/sessions');
  });

  it('Should allow user to delete their account when not admin', () => {
    // Reconfigure pour un utilisateur non-admin
    cy.window().then((window) => {
      window.localStorage.setItem(
        'session',
        JSON.stringify({
          token: 'fake-token',
          type: 'Bearer',
          id: 2,
          username: 'user@studio.com',
          firstName: 'User',
          lastName: 'Test',
          admin: false,
        })
      );
    });

    // Intercepte la requête utilisateur pour un utilisateur non-admin
    cy.intercept('GET', '/api/user/2', {
      statusCode: 200,
      body: {
        id: 2,
        email: 'user@studio.com',
        lastName: 'Test',
        firstName: 'User',
        admin: false,
        createdAt: '2023-01-01T00:00:00.000Z',
        updatedAt: '2023-01-01T00:00:00.000Z',
      },
    }).as('getNonAdminUser');

    // Intercepte la requête de suppression
    cy.intercept('DELETE', '/api/user/2', {
      statusCode: 200,
    }).as('deleteUser');

    // Recharge la page avec les nouvelles données
    cy.visit('/me');
    cy.wait('@getNonAdminUser');

    // Vérifie qu'un utilisateur non-admin peut voir le bouton de suppression
    cy.contains('p', 'Delete my account:').should('be.visible');

    // Prépare l'interception du dialogue de confirmation
    cy.on('window:confirm', () => true);

    // Sélectionne le bouton de suppression avec l'icône "delete" et le texte "Detail"
    cy.get('button[color="warn"]').should('be.visible').click();

    // Attend que la requête de suppression soit effectuée
    cy.wait('@deleteUser');

    // Vérifie la redirection vers la page d'accueil
    cy.url().should('include', '/');

    // Vérifie que les données de session ont été effacées
    cy.window().then((window) => {
      expect(window.localStorage.getItem('session')).to.be.null;
    });
  });
});
