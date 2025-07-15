import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'numberFormat'
})
export class NumberFormatPipe implements PipeTransform {

  transform(value: number): string {
    return new Intl.NumberFormat('fr-FR', { useGrouping: true }).format(value);
  }

}
