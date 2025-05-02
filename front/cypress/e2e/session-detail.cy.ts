describe('Session Detail spec', () => {
  // Données pour la liste des sessions
  const sessionsData = [
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
      name: 'Session de Yoga',
      description: 'Description détaillée de la session de yoga',
      date: '2025-04-26T14:00:00.000Z',
      teacher_id: 2,
      users: [3, 4],
      createdAt: '2025-04-21T10:00:00.000Z',
      updatedAt: '2025-04-21T10:00:00.000Z',
    },
  ];

  // Données pour l'enseignant
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

      // Intercepte les requêtes pour la liste des sessions
      cy.intercept('GET', '/api/session', {
        body: sessionsData,
      }).as('getSessions');

      // Visite directement la page des détails d'une session
      cy.visit('/sessions');
      cy.wait('@getSessions');
    });

    it('Should navigate to session details when clicking Detail button and show session information', () => {
      // Configure l'interception avant de cliquer
      cy.intercept('GET', '/api/session/1', {
        body: sessionsData[0],
      }).as('getSessionDetail');

      cy.intercept('GET', '/api/teacher/1', {
        body: {
          id: 1,
          firstName: 'Jane',
          lastName: 'Smith',
          email: 'jane.smith@example.com',
        },
      }).as('getTeacherDetail');

      // Clique sur le bouton Detail pour la première session
      cy.contains('Session 1')
        .parents('mat-card')
        .contains('button', 'Detail')
        .click();

      // Vérifie la navigation vers la page de détails
      cy.url().should('include', '/sessions/detail/1');

      // Attend que les requêtes soient complétées
      cy.wait('@getSessionDetail');
      cy.wait('@getTeacherDetail');

      // Vérifie les informations affichées
      cy.contains('h1', 'Session 1').should('be.visible');
      cy.contains('span', 'Jane SMITH').should('be.visible');
      cy.contains('span', '0 attendees').should('be.visible');
      cy.contains('span', 'April 25, 2025').should('be.visible');
    });

    it('Should navigate back to sessions list when back button is clicked', () => {
      // Configure l'interception pour la page de détails
      cy.intercept('GET', '/api/session/1', {
        body: sessionsData[0],
      }).as('getSessionDetail');

      cy.intercept('GET', '/api/teacher/1', {
        body: {
          id: 1,
          firstName: 'Jane',
          lastName: 'Smith',
          email: 'jane.smith@example.com',
        },
      }).as('getTeacherDetail');

      // Visite directement la page des détails
      cy.visit('/sessions/detail/1');
      cy.wait(['@getSessionDetail', '@getTeacherDetail']);

      // Intercepte la requête pour la liste des sessions (pour le retour)
      cy.intercept('GET', '/api/session', {
        body: sessionsData,
      }).as('getSessionsAfterBack');

      // Clique sur le bouton de retour
      cy.get('button').find('mat-icon').contains('arrow_back').click();

      // Vérifie la redirection vers la liste des sessions
      cy.url().should('include', '/sessions');
      cy.url().should('not.include', '/detail');
    });

    it('Should delete session and navigate back to sessions list when delete button is clicked', () => {
      // Configure l'interception pour la page de détails
      cy.intercept('GET', '/api/session/1', {
        body: sessionsData[0],
      }).as('getSessionDetail');

      cy.intercept('GET', '/api/teacher/1', {
        body: {
          id: 1,
          firstName: 'Jane',
          lastName: 'Smith',
          email: 'jane.smith@example.com',
        },
      }).as('getTeacherDetail');

      // Visite directement la page des détails
      cy.visit('/sessions/detail/1');
      cy.wait(['@getSessionDetail', '@getTeacherDetail']);

      // Intercepte la requête de suppression
      cy.intercept('DELETE', '/api/session/1', {
        statusCode: 200,
      }).as('deleteSession');

      // Intercepte la requête pour la liste des sessions (après suppression)
      cy.intercept('GET', '/api/session', {
        body: [sessionsData[1]], // Retourne seulement la deuxième session (la première a été supprimée)
      }).as('getSessionsAfterDelete');

      // Clique sur le bouton de suppression
      cy.get('button[color="warn"]').contains('Delete').click();

      // Attend que la requête de suppression soit terminée
      cy.wait('@deleteSession');

      // Vérifie la redirection vers la liste des sessions
      cy.url().should('include', '/sessions');
      cy.url().should('not.include', '/detail');
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

      // Intercepte les requêtes pour la liste des sessions
      cy.intercept('GET', '/api/session', {
        body: sessionsData,
      }).as('getSessions');

      // Commence à la page des sessions
      cy.visit('/sessions');
      cy.wait('@getSessions');
    });

    it('Should navigate to session details and not show delete button', () => {
      // Configure l'interception avant de cliquer
      cy.intercept('GET', '/api/session/1', {
        body: sessionsData[0],
      }).as('getSessionDetail');

      cy.intercept('GET', '/api/teacher/1', {
        body: {
          id: 1,
          firstName: 'Jane',
          lastName: 'Smith',
          email: 'jane.smith@example.com',
        },
      }).as('getTeacherDetail');

      // Clique sur le bouton Detail de la première session
      cy.contains('Session 1')
        .parents('mat-card')
        .contains('button', 'Detail')
        .click();

      // Vérifie la navigation vers la page de détails
      cy.url().should('include', '/sessions/detail/1');

      // Attend les requêtes
      cy.wait('@getSessionDetail');
      cy.wait('@getTeacherDetail');

      // Vérifie que le bouton de suppression n'est pas présent pour les utilisateurs non-admin
      cy.contains('button', 'Delete').should('not.exist');

      // Vérifie que le bouton "Participate" est affiché car l'utilisateur ne participe pas (users: [])
      cy.contains('button', 'Participate').should('be.visible');
    });

    it('Should handle "Do not participate" action', () => {
      // Configure les interceptions avant de visiter la page
      cy.intercept('GET', '/api/session/2', {
        body: sessionsData[1],
      }).as('getSessionDetail');

      cy.intercept('GET', '/api/teacher/2', {
        body: teacherData,
      }).as('getTeacherDetail');

      // Visite directement la page des détails de la session 2
      cy.visit('/sessions/detail/2');
      cy.wait(['@getSessionDetail', '@getTeacherDetail']);

      // Intercepte la requête de désinscription
      cy.intercept('DELETE', '/api/session/2/participate/3', {
        statusCode: 200,
      }).as('unParticipate');

      // Intercepte la requête pour rafraîchir les détails de la session après désinscription
      cy.intercept('GET', '/api/session/2', {
        body: {
          ...sessionsData[1],
          users: [4], // L'utilisateur 3 a été retiré
        },
      }).as('refreshSessionDetail');

      // Clique sur le bouton pour ne plus participer
      cy.contains('button', 'Do not participate').click();

      // Attend que la requête de désinscription soit terminée
      cy.wait('@unParticipate');

      // Vérifie que le bouton "Participate" est maintenant affiché
      cy.contains('button', 'Participate').should('be.visible');
      cy.contains('button', 'Do not participate').should('not.exist');
    });

    it('Should handle "Participate" action', () => {
      // Simule un utilisateur connecté non-admin avec ID 5 (non-participant)
      cy.window().then((window) => {
        window.localStorage.setItem(
          'session',
          JSON.stringify({
            token: 'fake-token',
            type: 'Bearer',
            id: 5,
            username: 'user5@studio.com',
            firstName: 'Regular',
            lastName: 'User',
            admin: false,
          })
        );
      });

      // Configure les interceptions avant de visiter la page
      cy.intercept('GET', '/api/session/2', {
        body: sessionsData[1], // Cet utilisateur (ID 5) ne fait pas partie des participants [3, 4]
      }).as('getSessionDetail');

      cy.intercept('GET', '/api/teacher/2', {
        body: teacherData,
      }).as('getTeacherDetail');

      // Visite directement la page des détails
      cy.visit('/sessions/detail/2');
      cy.wait(['@getSessionDetail', '@getTeacherDetail']);

      // Vérifie que le bouton "Participate" est affiché initialement
      cy.contains('button', 'Participate').should('be.visible');

      // Intercepte la requête d'inscription
      cy.intercept('POST', '/api/session/2/participate/5', {
        statusCode: 200,
      }).as('participate');

      // Intercepte la requête pour rafraîchir les détails de la session après inscription
      cy.intercept('GET', '/api/session/2', {
        body: {
          ...sessionsData[1],
          users: [3, 4, 5], // L'utilisateur 5 a été ajouté
        },
      }).as('refreshSessionDetail');

      // Clique sur le bouton pour participer
      cy.contains('button', 'Participate').click();

      // Attend que la requête d'inscription soit terminée
      cy.wait('@participate');

      // Vérifie que le bouton "Do not participate" est maintenant affiché
      cy.contains('button', 'Do not participate').should('be.visible');
      cy.contains('button', 'Participate').should('not.exist');
    });
  });
});
