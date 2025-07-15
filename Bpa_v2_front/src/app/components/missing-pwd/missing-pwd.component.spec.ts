import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MissingPwdComponent } from './missing-pwd.component';

describe('MissingPwdComponent', () => {
  let component: MissingPwdComponent;
  let fixture: ComponentFixture<MissingPwdComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ MissingPwdComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MissingPwdComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
