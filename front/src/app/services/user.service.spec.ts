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

  it('should delete a user', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`api/user/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
