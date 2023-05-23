import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BASEURL } from '../constants';
import { Po } from './po';
import { GenericHttpService} from '../generic-http.service';
@Injectable({
  providedIn: 'root'
})
export class PoService extends GenericHttpService<Po> {
  constructor(public http: HttpClient) {
    super(http, `${BASEURL}/api/pos`);
  } // constructor
} // EmployeeService
