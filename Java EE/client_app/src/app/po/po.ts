import {PoItem} from './po-item';

export interface Po {
  id: number;
  amount: number;
  vendorid: number;
  items: PoItem[];
  datecreated: Date;
}
