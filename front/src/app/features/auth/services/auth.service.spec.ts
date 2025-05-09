import { HttpClientModule, HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { AuthService } from './auth.service';
import { LoginRequest } from '../interfaces/loginRequest.interface';
import { RegisterRequest } from '../interfaces/registerRequest.interface';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';

describe('AuthService', () => {
  let service: AuthService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        AuthService
      ]
    });
    service = TestBed.inject(AuthService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify(); // Vérifie qu'il n'y a pas de requêtes en attente
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('register', () => {
    it('should send a POST request to register a new user', () => {
      const registerRequest: RegisterRequest = {
        email: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123'
      };

      service.register(registerRequest).subscribe(response => {
        expect(response).toBeUndefined(); // La méthode register retourne void
      });

      const req = httpMock.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(registerRequest);

      req.flush(null); // La réponse est vide (void)
    });

    it('should handle registration errors', () => {
      const registerRequest: RegisterRequest = {
        email: 'existing@example.com', // Email déjà utilisé
        firstName: 'John',
        lastName: 'Doe',
        password: 'password123'
      };

      // Spy sur console.error pour éviter les logs pendant les tests
      jest.spyOn(console, 'error').mockImplementation(() => {});

      service.register(registerRequest).subscribe({
        next: () => fail('should have failed with a 409'),
        error: (error) => {
          expect(error.status).toBe(409);
        }
      });

      const req = httpMock.expectOne('api/auth/register');
      expect(req.request.method).toBe('POST');

      req.flush('Email already exists', { status: 409, statusText: 'Conflict' });
    });
  });

  describe('login', () => {
    it('should send a POST request to login and return session information', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'password123'
      };

      const mockResponse: SessionInformation = {
        token: 'fake-jwt-token',
        type: 'Bearer',
        id: 1,
        username: 'test@example.com',
        firstName: 'John',
        lastName: 'Doe',
        admin: false
      };

      service.login(loginRequest).subscribe(response => {
        expect(response).toEqual(mockResponse);
      });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(loginRequest);

      req.flush(mockResponse);
    });

    it('should handle login errors with invalid credentials', () => {
      const loginRequest: LoginRequest = {
        email: 'wrong@example.com',
        password: 'wrongpassword'
      };

      // Spy sur console.error pour éviter les logs pendant les tests
      jest.spyOn(console, 'error').mockImplementation(() => {});

      service.login(loginRequest).subscribe({
        next: () => fail('should have failed with a 401'),
        error: (error) => {
          expect(error.status).toBe(401);
        }
      });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');

      req.flush('Invalid credentials', { status: 401, statusText: 'Unauthorized' });
    });

    it('should handle server errors during login', () => {
      const loginRequest: LoginRequest = {
        email: 'test@example.com',
        password: 'password123'
      };

      // Spy sur console.error pour éviter les logs pendant les tests
      jest.spyOn(console, 'error').mockImplementation(() => {});

      service.login(loginRequest).subscribe({
        next: () => fail('should have failed with a 500'),
        error: (error) => {
          expect(error.status).toBe(500);
        }
      });

      const req = httpMock.expectOne('api/auth/login');
      expect(req.request.method).toBe('POST');

      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });
});