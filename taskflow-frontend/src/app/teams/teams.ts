import { Component, OnInit } from '@angular/core';
import { TeamService } from '../services/team.service';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Navbar } from '../shared/navbar/navbar';
import { CreateTeamForm } from './create-team-form';
import { HasRoleDirective } from '../directives/has-role.directive';

@Component({
  selector: 'app-teams',
  templateUrl: './teams.html',
  standalone: true,
  imports: [FormsModule, CommonModule, Navbar, CreateTeamForm, HasRoleDirective],
})
export class Teams implements OnInit {
  teams: any[] = [];
  selectedTeam: any = null;

  totalMembers = 0;
  activeTasks = 0;

  showCreateForm = false;

  users: any[] = [];

  showMemberDropdown = false;
  selectedMemberId: any = null;

  constructor(private teamService: TeamService) {}

  ngOnInit() {
    this.loadTeams();
  }

  openCreateTeam() {
    this.showCreateForm = true;
  }

  closeCreateTeam() {
    this.showCreateForm = false;
  }

  loadTeams() {
    this.teamService.getTeams().subscribe((res: any) => {
      this.teams = res;

      this.totalMembers = res.reduce((a: any, t: any) => a + (t.members?.length || 0), 0);
    });
  }

  viewTeam(team: any) {
    this.selectedTeam = team;
  }

  deleteTeam(id: number) {
    if (!confirm('Delete team?')) return;

    this.teamService.deleteTeam(id).subscribe(() => this.loadTeams());
  }

  openMemberDropdown() {
    this.showMemberDropdown = true;

    this.loadMembers();
  }

  loadMembers() {
    this.teamService.getUsers().subscribe((res: any) => {
      this.users = res.filter((u: any) => u.role === 'MEMBER');
    });
  }

  addMember() {
    if (!this.selectedMemberId) return;

    this.teamService.addMember(this.selectedTeam.id, this.selectedMemberId).subscribe(() => {
      this.showMemberDropdown = false;

      this.loadTeams();
    });
  }

  removeMember(userId: number) {
    this.teamService.removeMember(this.selectedTeam.id, userId).subscribe(() => this.loadTeams());
  }
}
