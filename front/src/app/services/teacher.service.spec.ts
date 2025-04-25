import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { environment } from 'src/environments/environment';
import { Teacher } from '../interfaces/teacher.interface';

import { TeacherService } from './teacher.service';

describe('TeacherService', () => {
  let service: TeacherService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule
      ],
      providers: [TeacherService]
    });
    service = TestBed.inject(TeacherService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should retrieve all teachers', () => {
    const mockTeachers: Teacher[] = [
      {
        id: 1,
        lastName: 'DELAHAYE',
        firstName: 'Margot',
        createdAt: new Date(),
        updatedAt: new Date()
      },
      {
        id: 2,
        lastName: 'THIERCELIN',
        firstName: 'Hélène',
        createdAt: new Date(),
        updatedAt: new Date()
      }
    ];

    service.all().subscribe(teachers => {
      expect(teachers).toEqual(mockTeachers);
      expect(teachers.length).toBe(2);
    });

    const req = httpMock.expectOne(`api/teacher`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTeachers);
  });

  it('should handle errors when retrieving teachers', () => {
    // Spy on console.error to prevent actual logging in tests
    jest.spyOn(console, 'error').mockImplementation(() => {});

    service.all().subscribe({
      next: () => fail('should have failed with an error'),
      error: (error) => {
        expect(error.status).toBe(500);
      }
    });

    const req = httpMock.expectOne(`api/teacher`);
    req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });

    // Restore console.error
    jest.restoreAllMocks();
  });

  it('should get a teacher by id', () => {
    const mockTeacher: Teacher = {
      id: 1,
      lastName: 'DELAHAYE',
      firstName: 'Margot',
      createdAt: new Date(),
      updatedAt: new Date()
    };

    service.detail('1').subscribe(teacher => {
      expect(teacher).toEqual(mockTeacher);
    });

    const req = httpMock.expectOne(`api/teacher/1`);
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher);
  });
});
