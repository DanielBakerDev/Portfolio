import { AbstractControl } from '@angular/forms';

export function ValidatePhone(control: AbstractControl): { invalidPhone: boolean } {
  const PHONE_REGEXP = /^[(]{0,1}[0-9]{3}[)\.\- ]{0,1}[0-9]{3}[\.\- ]{0,1}[0-9]{4}$/;
  return !PHONE_REGEXP.test(control.value) ? {invalidPhone: true} : null;
} // ValidatePhone

export function ValidateEmail(control: AbstractControl): { invalidEmail: boolean } {
  const EMAIL_REGEXP = /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/;
  return !EMAIL_REGEXP.test(control.value) ? {invalidEmail: true} : null;
} // ValidatePhone

export function ValidatePostalCode(control: AbstractControl): { invalidPostalcode: boolean } {
  const POSTALCODE_REGEXP = /^[A-Za-z]\d[A-Za-z][ -]?\d[A-Za-z]\d$/;
  return !POSTALCODE_REGEXP.test(control.value) ? {invalidPostalcode: true} : null;
} // ValidatePhone

export function ValidateNumber(control: AbstractControl): { invalidNumber: boolean } {
  const NUMBER_REGEXP = /^\d+$/;
  return !NUMBER_REGEXP.test(control.value) ? {invalidNumber: true} : null;
} // ValidateNumber

export function ValidateDecimal(control: AbstractControl): { invalidDecimal: boolean } {
  const DECIMAL_REGEXP = /^(\d*\.)?\d+$/;
  return !DECIMAL_REGEXP.test(control.value) ? {invalidDecimal: true} : null;
} // ValidateNumber
