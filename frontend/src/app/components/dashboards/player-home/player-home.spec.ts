import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PlayerHome } from './player-home';

describe('PlayerHome', () => {
  let component: PlayerHome;
  let fixture: ComponentFixture<PlayerHome>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [PlayerHome]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PlayerHome);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
