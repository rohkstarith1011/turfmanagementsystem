import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamCreate } from './team-create';

describe('TeamCreate', () => {
  let component: TeamCreate;
  let fixture: ComponentFixture<TeamCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [TeamCreate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TeamCreate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
