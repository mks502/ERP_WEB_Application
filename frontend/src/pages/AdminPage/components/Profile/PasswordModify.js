import React, { Component } from 'react';
import axios from 'axios';

class PasswordModify extends Component {
    constructor(props) {
        super(props);
        this.state = { value: 'select', password:''};
    }

    handleChangeInput = (e, target) => {
        //인풋 값 변경
        console.log(e.target.value)
        this.setState({ [target]: e.target.value });
    }

    modifyPasswordAPI(){
        const authInfo={
          password: this.state.password
        }
        console.log("보낸다 가라아아앗",this.props.selectUser)
        console.log("보낸다 가라아아앗2",authInfo)
    
        return axios.post('http://localhost:8080/upmureport/api/users/modify',
        {
          mem:this.props.selectUser,
          authInfo:authInfo
        }
        )
      }



    render() {
        return (
            <div className="text-gray-900 p-3 m-0"> <b>비밀번호변경:  </b>
                <input type="password" value={this.state.posi} name="posi" onChange={e => this.handleChangeInput(e, 'password')} ></input>
                <input type="button" value=" Change " name="authInfo" onClick={this.modifyPasswordAPI.bind(this)} class="btn btn-success btn-icon-split"></input>
            </div>
        );
    }
}

export default PasswordModify;