import { Component, OnInit, OnDestroy} from '@angular/core';
import { FormControl, FormGroup, FormBuilder} from '@angular/forms';
import { Observable, of, Subscription} from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import { Vendor } from '../../vendor/vendor';
import { Product } from '../../product/product';
import { PoItem } from '../po-item';
import { Po } from '../po';
import { BASEURL, PDFURL } from '../../constants';
import { VendorService } from '../../vendor/vendor.service';
import { ProductService } from '../../product/product.service';
import { PoService } from '../po.service';
@Component({
  templateUrl: './po-generator.component.html'
})
export class PoGeneratorComponent implements OnInit, OnDestroy {
  // form
  generatorForm: FormGroup;
  vendorid: FormControl;
  productid: FormControl;
  eoq: FormControl;
  subscription: Subscription;
  products$: Observable<Product[]>; // everybody's products
  vendors$: Observable<Vendor[]>; // all vendors
  vendorproducts$: Observable<Product[]>; // all products for a particular vendor
  items: Array<PoItem>; // product items that will be in report
  selectedProducts: Product[]; // products that being displayed currently in app
  selectedProduct: Product; // the current selected product
  selectedVendor: Vendor; // the current selected vendor
  pickedProduct: boolean;
  pickedVendor: boolean;
  pickedQty: boolean;
  generated: boolean;
  hasProducts: boolean;
  msg: string;
  total: number;
  tax: number;
  sub: number;
  url: string;
  EOQ: number;
  reportno: number;
  constructor(private builder: FormBuilder,
              private vendorService: VendorService,
              private productService: ProductService,
              private poService: PoService) {
    this.pickedVendor = false;
    this.pickedProduct = false;
    this.pickedQty = false;
    this.generated = false;
    this.url = BASEURL + 'pos';
  } // constructor
  ngOnInit(): void {
    this.msg = '';
    this.vendorid = new FormControl('');
    this.productid = new FormControl('');
    this.eoq = new FormControl('');
    this.generatorForm = this.builder.group({
      productid: this.productid,
      vendorid: this.vendorid,
      eoq: this.eoq
    });
    this.onPickVendor();
    this.onPickProduct();
    this.onPickQty();
    this.msg = 'loading vendors and products from server...';
    this.vendors$ = this.vendorService.getAll().pipe(
      catchError(error => {
        if (error.error instanceof ErrorEvent) {
          this.msg = `Error: ${error.error.message}`;
        } else {
          this.msg = `Error: ${error.message}`;
        }
        return of([]); // returns an empty array if there's a problem
      })
    );
    this.products$ = this.productService.getAll().pipe(
      catchError(error => {
        if (error.error instanceof ErrorEvent) {
          this.msg = `Error: ${error.error.message}`;
        } else {
          this.msg = `Error: ${error.message}`;
        }
        return of([]);
      })
    );
    this.msg = 'server data loaded';
  } // ngOnInit
  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  } // ngOnDestroy
  /**
   * onPickVendor - Another way to use Observables, subscribe to the select change event
   * then load specific vendor products for subsequent selection
   */
  onPickVendor(): void {
    this.subscription = this.generatorForm.get('vendorid').valueChanges.subscribe(val => {
      this.selectedProduct = null;
      this.selectedVendor = val;
      this.loadVendorProducts();
      this.pickedProduct = false;
      this.hasProducts = false;
      this.msg = 'choose product for vendors';
      this.pickedVendor = true;
      this.generated = false;
      this.items = []; // array for the po
      this.selectedProducts = []; // array for the details in app html
    });
  } // onPickVendor
  /**
   * onPickProduct - subscribe to the select change event then
   * update array containing items.
   */
  onPickProduct(): void {
    const xSubscr = this.generatorForm.get('productid').valueChanges.subscribe(val => {
      this.selectedProduct = val;
      this.pickedProduct = true;
      this.msg = 'choose qty of product';
      this.loadProductQty();
    });
    this.subscription.add(xSubscr); // add it as a child, so all can be destroyed together
  }
  onPickQty(): void {
    const xSubscr = this.generatorForm.get('eoq').valueChanges.subscribe(val => {
      const item: PoItem = {id: 0, poid: 0, productid: this.selectedProduct.id, qty: val, price: this.selectedProduct.msrp};
      if (this.items.find(it => it.productid === this.selectedProduct.id)) { // ignore entry
        const ind: number =  this.items.findIndex(it => it.productid === this.selectedProduct.id);

        if (val === 0){
          this.items.splice(ind, 1);
          this.msg = 'Item(s) deleted';
        }else{
          this.selectedProduct[ind].qty = val;
        }

      } else { // add entry
        this.items.push(item);
        this.selectedProducts.push(this.selectedProduct);
      }
      if (this.items.length > 0) {
        this.hasProducts = true;
      }
      this.sub = 0.0;
      this.tax = 0.0;
      this.total = 0.0;

      this.items.forEach(exp => this.sub += exp.price * exp.qty);
      this.items.forEach(exp => this.tax += (exp.price * exp.qty) * 0.13);
      this.total = this.sub + this.tax;
    });
    this.subscription.add(xSubscr); // add it as a child, so all can be destroyed together
  }
  loadProductQty(): void {
    if (this.selectedProduct){
      this.EOQ = this.selectedProduct.eoq;
    }else{
      this.EOQ = 0;
    }
  }
  /**
   * loadVendorProducts - filter for a particular vendor's products
   */
  loadVendorProducts(): void {
    this.vendorproducts$ = this.products$.pipe(
      map( products =>
        // map each products in the array and check whether or not it belongs to selected vendorer
        products.filter(product => product.vendorid === this.selectedVendor.id)
      )
    );
  } // loadVendorProducts

  /**
   * createPO - create the client side po
   */
  createPo(): void {
    this.generated = false;
    const po: Po = {id: 0, items: this.items, vendorid: this.selectedProduct.vendorid, amount: this.total, datecreated: null};
    const rSubscr = this.poService.add(po).subscribe(
      payload => { // server should be returning new id
        if (typeof payload === 'number') {
          this.msg = `Po ${payload} added!`;
          this.reportno = payload;
          this.generated = true;
        } else {
          this.msg = 'Po not added! - server error';
        }
        this.hasProducts = false;
        this.pickedVendor = false;
        this.pickedProduct = false;
        this.pickedQty = false;
      },
      err => {
        this.msg = `Error - product not added - ${err.status} - ${err.statusText}`;
      });
    this.subscription.add(rSubscr); // add it as a child, so all can be destroyed together
  } // createPo
  viewPdf(): void {
    window.open(PDFURL + this.reportno, '');
  } // viewPdf
} // PoGeneratorComponent
