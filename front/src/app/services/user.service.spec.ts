import { HttpClientModule, HttpClient } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { environment } from 'src/environments/environment';

import { UserService } from './user.service';
import { User } from '../interfaces/user.interface';

describe('UserService', () => {
  let service: UserService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule
      ],
      providers: [
        UserService
      ]
    });
    service = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
    httpClient = TestBed.inject(HttpClient);
  });

  afterEach(() => {
    httpMock.verify(); // S'assure qu'il n'y a pas de requêtes en attente
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should get user by id', () => {
    const mockUser: User = {
      id: 1,
      firstName: 'John',
      lastName: 'Doe',
      email: 'john.doe@example.com',
      admin: false,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.getById('1').subscribe(user => {
      expect(user).toEqual(mockUser);
    });

    const req = httpMock.expectOne(`api/user/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
  });

  it('should handle error when getting user by id', () => {
    // Spy sur console.error pour éviter les logs pendant les tests
    jest.spyOn(console, 'error').mockImplementation(() => {});

    service.getById('999').subscribe({
      next: () => fail('should have failed with a 404'),
      error: (error) => {
        expect(error.status).toBe(404);
      }
    });

    const req = httpMock.expectOne(`api/user/999`);
    req.flush('User not found', { status: 404, statusText: 'Not Found' });
  });

  it('should delete user by id', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`api/user/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should handle error when deleting user', () => {
    // Spy sur console.error pour éviter les logs pendant les tests
    jest.spyOn(console, 'error').mockImplementation(() => {});

    service.delete('999').subscribe({
      next: () => fail('should have failed with a 403'),
      error: (error) => {
        expect(error.status).toBe(403);
      }
    });

    const req = httpMock.expectOne(`api/user/999`);
    req.flush('Forbidden', { status: 403, statusText: 'Forbidden' });
  });
});
