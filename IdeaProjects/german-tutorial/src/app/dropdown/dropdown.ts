import { Component } from '@angular/core';
import {NgForOf} from '@angular/common';

@Component({
  selector: 'app-dropdown',
  templateUrl: 'dropdown.html',
  imports: [
    NgForOf
  ],
  styleUrl: 'dropdown.css'
})
export class Dropdown {
  players = [
    "Artikel",
    "Pronomen",
    "Verbs",
    "Preposition"
  ]
  selected: string = "----"

  update(e:any){
    this.selected = e.target.value
  }



}
