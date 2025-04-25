import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { environment } from 'src/environments/environment';
import { Session } from '../interfaces/session.interface';

import { SessionApiService } from './session-api.service';

describe('SessionApiService', () => {
  let service: SessionApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [SessionApiService]
    });
    service = TestBed.inject(SessionApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all sessions', () => {
    const mockSessions: Session[] = [
      {
        id: 1,
        name: 'Session 1',
        description: 'Description 1',
        date: new Date(),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        id: 2,
        name: 'Session 2',
        description: 'Description 2',
        date: new Date(),
        teacher_id: 2,
        users: [1],
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];

    service.all().subscribe(sessions => {
      expect(sessions).toEqual(mockSessions);
      expect(sessions.length).toBe(2);
    });

    const req = httpMock.expectOne(`api/session`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSessions);
  });

  it('should retrieve a session by id', () => {
    const mockSession: Session = {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.detail('1').subscribe(session => {
      expect(session).toEqual(mockSession);
    });

    const req = httpMock.expectOne(`api/session/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockSession);
  });

  it('should create a new session', () => {
    const newSession: Session = {
      name: 'New Session',
      description: 'New Description',
      date: new Date(),
      teacher_id: 1,
      users: [],
      createdAt: undefined,
      updatedAt: undefined
    };

    const mockResponse = { ...newSession, id: 3 };

    service.create(newSession).subscribe(response => {
      expect(response).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`api/session`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(newSession);
    req.flush(mockResponse);
  });

  it('should update a session', () => {
    const updatedSession: Session = {
      id: 1,
      name: 'Updated Session',
      description: 'Updated Description',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date()
    };

    // Utilisation correcte de update avec 2 paramètres: id, session
    service.update('1', updatedSession).subscribe(response => {
      expect(response).toEqual(updatedSession);
    });

    const req = httpMock.expectOne(`api/session/1`);
    expect(req.request.method).toBe('PUT');
    expect(req.request.body).toEqual(updatedSession);
    req.flush(updatedSession);
  });

  it('should delete a session', () => {
    service.delete('1').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`api/session/1`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });

  it('should participate in a session', () => {
    service.participate('1', '2').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`api/session/1/participate/2`);
    expect(req.request.method).toBe('POST');
    req.flush({});
  });

  it('should unparticipate from a session', () => {
    service.unParticipate('1', '2').subscribe(response => {
      expect(response).toBeTruthy();
    });

    const req = httpMock.expectOne(`api/session/1/participate/2`);
    expect(req.request.method).toBe('DELETE');
    req.flush({});
  });
});
