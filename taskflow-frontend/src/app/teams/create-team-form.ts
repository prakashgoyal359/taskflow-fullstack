import { Component, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TeamService } from '../services/team.service';

@Component({
selector:'app-create-team-form',
standalone:true,
imports:[FormsModule,CommonModule],
templateUrl:'./create-team-form.html'
})
export class CreateTeamForm{

@Output() close = new EventEmitter<void>();
@Output() refresh = new EventEmitter<void>();

team:any={
name:'',
description:''
};

constructor(private teamService:TeamService){}

save(){

if(!this.team.name) return;

this.teamService.createTeam(this.team)
.subscribe(()=>{

this.refresh.emit();
this.close.emit();

});
}

cancel(){
this.close.emit();
}

}