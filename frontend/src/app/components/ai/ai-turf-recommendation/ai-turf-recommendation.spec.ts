import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AiTurfRecommendation } from './ai-turf-recommendation';

describe('AiTurfRecommendation', () => {
  let component: AiTurfRecommendation;
  let fixture: ComponentFixture<AiTurfRecommendation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AiTurfRecommendation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AiTurfRecommendation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
