import axios from 'axios';
import React, { Component } from 'react';
import { DropDownSettings } from './dropDown.interface';

export class DropDown extends Component<any, DropDownSettings> {
  public state: DropDownSettings = {
    categories: [],
    categ: ''
  };

  constructor(props: DropDownSettings) {
    super(props);
    this.handleChange = this.handleChange.bind(this);
  }

  public componentDidMount() {
    axios.get(`http://localhost:8080/event/allCategory`).then((res) => {
      const categories = res.data;
      this.setState({ categories });
    });
  }
  public componentWillMount() {
    axios.get(`http://localhost:8080/event/allCategory`).then((res) => {
      const categories = res.data;
      this.setState({ categories });
    });
  }

  public handleChange(e: any) {
    this.props.onCategoryChange(e.target.value);
  }

  public render() {
    if (typeof this.state.categories === 'undefined') {
      console.log('I during componentDidMount ' + this.state.categories);
      return <div />;
    } else {
      return (

        <div className="input-group">
          <select className="custom-select select" onChange={this.handleChange}>
            {this.state.categories.map((option, index) => (
              <option key={index} value={option.categoryName}>
                {option.categoryName}
              </option>
            ))}
          </select>
        </div>
      );
    }
  }
}
