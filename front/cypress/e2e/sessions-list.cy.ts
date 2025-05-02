describe('Sessions List spec', () => {
  const mockSessions = [
    {
      id: 1,
      name: 'Yoga débutant',
      description: 'Séance pour débutants',
      date: new Date('2023-07-21T14:00:00'),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      name: 'Yoga intermédiaire',
      description: 'Séance pour pratiquants réguliers',
      date: new Date('2023-07-22T16:00:00'),
      teacher_id: 2,
      users: [3],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 3,
      name: 'Méditation guidée',
      description: 'Séance de relaxation profonde',
      date: new Date('2023-07-23T10:00:00'),
      teacher_id: 1,
      users: [],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  beforeEach(() => {
    // Simuler une connexion en injectant un token dans localStorage
    window.localStorage.setItem('session', JSON.stringify({
      token: 'fake-jwt-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    }));
    
    // Intercepter la requête API pour les sessions
    cy.intercept('GET', '/api/session', {
      body: mockSessions
    }).as('getSessions');
    
    // Visiter la page des sessions
    cy.visit('/sessions');
    cy.wait('@getSessions');
  });
  
  it('Should display sessions list correctly', () => {
    // Vérifier que les 3 sessions sont affichées
    cy.get('app-session-item').should('have.length', 3);
    
    // Vérifier les titres des sessions
    cy.contains('Yoga débutant').should('be.visible');
    cy.contains('Yoga intermédiaire').should('be.visible');
    cy.contains('Méditation guidée').should('be.visible');
    
    // Vérifier les dates formatées
    cy.contains('21/07/2023').should('be.visible');
    cy.contains('22/07/2023').should('be.visible');
    cy.contains('23/07/2023').should('be.visible');
  });
  
  it('Should navigate to session details when clicking on a session', () => {
    // Intercepter la requête pour les détails de la session
    cy.intercept('GET', '/api/session/1', {
      body: mockSessions[0]
    }).as('getSessionDetails');
    
    // Intercepter la requête pour le professeur
    cy.intercept('GET', '/api/teacher/1', {
      body: {
        id: 1,
        firstName: 'Jane',
        lastName: 'Smith',
        createdAt: new Date(),
        updatedAt: new Date()
      }
    }).as('getTeacher');
    
    // Cliquer sur la première session
    cy.contains('Yoga débutant').click();
    
    // Vérifier qu'on est redirigé vers la page de détails
    cy.url().should('include', '/sessions/1');
    cy.wait(['@getSessionDetails', '@getTeacher']);
    
    // Vérifier que les détails sont affichés
    cy.contains('Yoga débutant').should('be.visible');
    cy.contains('Séance pour débutants').should('be.visible');
  });
  
  it('Should display Create button for admin users only', () => {
    // Par défaut, l'utilisateur connecté n'est pas admin, le bouton ne devrait pas être visible
    cy.contains('button', 'Create').should('not.exist');
    
    // Déconnecter l'utilisateur actuel
    cy.window().then((win) => {
      win.localStorage.removeItem('session');
    });
    
    // Simuler une connexion en tant qu'admin
    cy.window().then((win) => {
      win.localStorage.setItem('session', JSON.stringify({
        token: 'fake-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'adminuser',
        firstName: 'Admin',
        lastName: 'User',
        admin: true
      }));
    });
    
    // Recharger la page
    cy.reload();
    cy.wait('@getSessions');
    
    // Vérifier que le bouton Create est maintenant visible
    cy.contains('button', 'Create').should('be.visible');
  });
  
  it('Should navigate to create form when clicking Create button', () => {
    // Déconnecter l'utilisateur actuel
    cy.window().then((win) => {
      win.localStorage.removeItem('session');
    });
    
    // Simuler une connexion en tant qu'admin
    cy.window().then((win) => {
      win.localStorage.setItem('session', JSON.stringify({
        token: 'fake-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'adminuser',
        firstName: 'Admin',
        lastName: 'User',
        admin: true
      }));
    });
    
    // Intercepter la requête pour les enseignants
    cy.intercept('GET', '/api/teacher', {
      body: [
        {
          id: 1,
          firstName: 'Jane',
          lastName: 'Smith',
          createdAt: new Date(),
          updatedAt: new Date()
        },
        {
          id: 2,
          firstName: 'John',
          lastName: 'Doe',
          createdAt: new Date(),
          updatedAt: new Date()
        }
      ]
    }).as('getTeachers');
    
    // Recharger la page
    cy.reload();
    cy.wait('@getSessions');
    
    // Cliquer sur le bouton Create
    cy.contains('button', 'Create').click();
    
    // Vérifier qu'on est redirigé vers le formulaire de création
    cy.url().should('include', '/sessions/create');
    
    // Vérifier que le formulaire est affiché
    cy.get('form').should('be.visible');
    cy.get('input[formControlName=name]').should('be.visible');
  });
  
  it('Should handle empty sessions list', () => {
    // Intercepter la requête API pour les sessions avec une liste vide
    cy.intercept('GET', '/api/session', {
      body: []
    }).as('getEmptySessions');
    
    // Recharger la page
    cy.reload();
    cy.wait('@getEmptySessions');
    
    // Vérifier qu'il n'y a pas de sessions affichées
    cy.get('app-session-item').should('not.exist');
    
    // Idéalement, vérifier le message d'absence de sessions
    // Note: ceci dépend de l'implémentation exacte
    // cy.contains('No sessions available').should('be.visible');
  });
});