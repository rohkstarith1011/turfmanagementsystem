import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CoachingClassView } from './coaching-class-view';

describe('CoachingClassView', () => {
  let component: CoachingClassView;
  let fixture: ComponentFixture<CoachingClassView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [CoachingClassView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CoachingClassView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
