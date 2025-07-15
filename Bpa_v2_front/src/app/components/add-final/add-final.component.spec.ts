import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddFinalComponent } from './add-final.component';

describe('AddFinalComponent', () => {
  let component: AddFinalComponent;
  let fixture: ComponentFixture<AddFinalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AddFinalComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddFinalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
