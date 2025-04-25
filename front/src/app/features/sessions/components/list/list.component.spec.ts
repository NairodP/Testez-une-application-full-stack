import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { DebugElement, NO_ERRORS_SCHEMA } from '@angular/core';
import { By } from '@angular/platform-browser';
import { BehaviorSubject, of } from 'rxjs';
import { expect } from '@jest/globals';
import { Session } from '../../interfaces/session.interface';
import { SessionService } from 'src/app/services/session.service';
import { ListComponent } from './list.component';
import { SessionApiService } from "../../services/session-api.service";
import { RouterTestingModule } from '@angular/router/testing';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;

  // Définition des sessions mockées
  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Session 1',
      date: new Date(),
      description: 'Yoga beginner',
      teacher_id: 101,
      users: [1, 2, 3],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      name: 'Session 2',
      date: new Date(),
      description: 'Advanced yoga',
      teacher_id: 102,
      users: [4, 5],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  const mockSessionService = {
    sessionInformation: {
      token: 'mock-token',
      type: 'Bearer',
      id: 1,
      username: 'testUser',
      firstName: 'Test',
      lastName: 'User',
      admin: true
    }
  };

  const mockSessionApiService = {
    all: jest.fn().mockReturnValue(of(mockSessions))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientTestingModule,
        MatCardModule,
        MatIconModule,
        RouterTestingModule.withRoutes([])
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);

    // Reset mocks
    jest.clearAllMocks();
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should have sessions$ defined upon creation', () => {
    // Le composant doit initialiser sessions$ lors de sa création
    expect(component.sessions$).toBeDefined();
  });

  it('should expose user from sessionService', () => {
    fixture.detectChanges();
    expect(component.user).toEqual(mockSessionService.sessionInformation);
  });

  it('should return undefined if session information is not available', () => {
    const emptySessionService = {
      sessionInformation: undefined
    };
    const testApiService = {
      all: jest.fn().mockReturnValue(of([]))
    };

    const testComponent = new ListComponent(
      emptySessionService as unknown as SessionService,
      testApiService as unknown as SessionApiService
    );

    expect(testComponent.user).toBeUndefined();
  });

  it('should display the "Create" button if the user is an admin', () => {
    fixture.detectChanges();

    const createButton = fixture.debugElement.query(By.css('button[routerLink="create"]'));
    expect(createButton).toBeTruthy();
  });
});
