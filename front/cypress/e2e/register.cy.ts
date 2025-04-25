describe('Register spec', () => {
  it('Register successfully', () => {
    cy.visit('/register');

    // Intercepte la requête d'inscription
    cy.intercept('POST', '/api/auth/register', {
      statusCode: 200,
      body: {
        id: 2,
        username: 'newuser@yoga.com',
        firstName: 'New',
        lastName: 'User',
        admin: false
      }
    }).as('register');

    // Intercepte la requête de connexion après inscription
    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 2,
        username: 'newuser@yoga.com',
        firstName: 'New',
        lastName: 'User',
        admin: false
      }
    }).as('login');

    // Remplit le formulaire d'inscription
    cy.get('input[formControlName=firstName]').type('New');
    cy.get('input[formControlName=lastName]').type('User');
    cy.get('input[formControlName=email]').type('newuser@yoga.com');
    cy.get('input[formControlName=password]').type('Test1234!');
    
    // Vérifie que le bouton est activé avant de cliquer
    cy.get('button[type=submit]').should('not.be.disabled').click();

    // Vérifie que la requête d'inscription a été interceptée
    cy.wait('@register');
    
    // Vérifie la redirection - ajustement: l'application peut rediriger vers /login avant /sessions
    cy.url().should('include', '/login').then(() => {
      // Login after registration
      cy.get('input[formControlName=email]').type("newuser@yoga.com");
      cy.get('input[formControlName=password]').type('Test1234!');
      cy.get('button[type=submit]').should('not.be.disabled').click();
      
      // Now we should be redirected to sessions
      cy.url().should('include', '/sessions');
    });
  });

  it('Register with invalid data', () => {
    cy.visit('/register');

    // Remplit partiellement le formulaire avec des données invalides
    cy.get('input[formControlName=firstName]').type('New');
    cy.get('input[formControlName=lastName]').type('User');
    // Email manquant intentionnellement
    cy.get('input[formControlName=password]').type('Test1234!');
    
    // Vérifie que le bouton est désactivé à cause de données invalides
    cy.get('button[type=submit]').should('be.disabled');
    
    // Vérifie qu'on reste sur la page d'inscription
    cy.url().should('include', '/register');
    
    // Test simplifié: vérifions simplement que le formulaire détecte les champs obligatoires
    cy.get('input[formControlName=email]').focus().blur();
    
    // Vérifie que le bouton est désactivé
    cy.get('button[type=submit]').should('be.disabled');
  });
});