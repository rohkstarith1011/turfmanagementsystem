import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityCreate } from './facility-create';

describe('FacilityCreate', () => {
  let component: FacilityCreate;
  let fixture: ComponentFixture<FacilityCreate>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FacilityCreate]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityCreate);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
