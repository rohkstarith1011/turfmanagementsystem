import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AiSlotRecommendation } from './ai-slot-recommendation';

describe('AiSlotRecommendation', () => {
  let component: AiSlotRecommendation;
  let fixture: ComponentFixture<AiSlotRecommendation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AiSlotRecommendation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AiSlotRecommendation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
