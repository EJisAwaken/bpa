import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UpdateFinalComponent } from './update-final.component';

describe('UpdateFinalComponent', () => {
  let component: UpdateFinalComponent;
  let fixture: ComponentFixture<UpdateFinalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UpdateFinalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UpdateFinalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
