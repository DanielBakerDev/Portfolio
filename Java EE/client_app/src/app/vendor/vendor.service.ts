import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BASEURL } from '../constants';
import { Vendor } from './vendor';
import { GenericHttpService} from '../generic-http.service';
@Injectable({
  providedIn: 'root'
})
export class VendorService extends GenericHttpService<Vendor> {
  constructor(public http: HttpClient) {
    super(http, `${BASEURL}/api/vendors`);
  } // constructor
} // VendorService
