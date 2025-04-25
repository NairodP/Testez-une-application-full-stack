import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';
import { SessionInformation } from '../interfaces/sessionInformation.interface';

describe('SessionService', () => {
  let service: SessionService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    // Clear localStorage before each test
    localStorage.clear();
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return logged$ as false when not logged in', (done) => {
    service.$isLogged().subscribe(value => {
      expect(value).toBeFalsy();
      done();
    });
  });

  it('should set session information and update isLogged$', (done) => {
    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    service.logIn(mockSession);

    service.$isLogged().subscribe(value => {
      expect(value).toBeTruthy();
      done();
    });

    expect(service.sessionInformation).toEqual(mockSession);
    expect(localStorage.getItem('session')).toEqual(JSON.stringify(mockSession));
  });

  it('should clear session information when logging out', (done) => {
    // Set up an initial logged-in state
    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };
    service.logIn(mockSession);

    // Log out
    service.logOut();

    // Verify logged out state
    expect(service.sessionInformation).toBeUndefined();
    expect(localStorage.getItem('session')).toBeNull();

    service.$isLogged().subscribe(value => {
      expect(value).toBeFalsy();
      done();
    });
  });

  it('should retrieve session from localStorage on initialization', () => {
    // D'abord, on nettoie le localStorage
    localStorage.clear();

    const mockSession: SessionInformation = {
      token: 'fake-token',
      type: 'Bearer',
      id: 1,
      username: 'testuser',
      firstName: 'Test',
      lastName: 'User',
      admin: false
    };

    // Manuellement définir le localStorage avant d'initialiser le service
    localStorage.setItem('session', JSON.stringify(mockSession));

    // Réinitialiser le service pour déclencher la logique du constructeur
    const newService = new SessionService();

    // Vérifier si le service a lu depuis localStorage
    expect(newService.sessionInformation).toEqual(mockSession);
    expect(newService.isLogged).toBeTruthy();
  });
});
