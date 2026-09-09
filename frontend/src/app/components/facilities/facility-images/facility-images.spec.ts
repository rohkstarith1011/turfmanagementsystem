import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityImages } from './facility-images';

describe('FacilityImages', () => {
  let component: FacilityImages;
  let fixture: ComponentFixture<FacilityImages>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FacilityImages]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityImages);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
