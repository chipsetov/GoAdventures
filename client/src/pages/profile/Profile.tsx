import { AxiosResponse } from 'axios';
import React, { Component } from 'react';
import { getUserData } from '../../api/user.service';
import { UserDto } from '../../interfaces/User.dto';
import './Profile.scss';
import { EditForm } from './sidebar/EditForm';
import Sidebar from './sidebar/Sidebar';

interface ProfileState {
  userProfile: UserDto;
  userEventList: any;
  showEditForm: boolean;
}

export class Profile extends Component<UserDto, ProfileState> {
  // початкова ініціалізація(null)
  constructor(props: any) {
    super(props);

    this.state = {
      showEditForm: true,
      userProfile: {
        fullName: '',
        userName: '',
        email: '',
        avatarUrl: ''
      },
      userEventList: {
        description: '',
        topic: '',
        start_date: ''
      }
    };
  }

  public componentDidMount() {
    // сеттер на пропси зверху з api
    getUserData().then((response: AxiosResponse<UserDto>) =>
      this.setState({
        userProfile: { ...response.data }
      })
    );
  }

  public render() {
    // рендер екземпляров сайдбар і юзерівенліст
    return (
      <div className='profile-page'>
        <div className='sidebar'>
          <Sidebar {...this.state.userProfile} />
        </div>
        <div className='Profile__content'>
          {this.state.showEditForm ? <EditForm /> : <div>User Event List</div>}
        </div>
      </div>
    );
  }
}
