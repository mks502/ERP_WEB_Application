import React, { Component } from 'react';
import axios from 'axios';
import { BASE_URL } from '../../../../supports/API_CONSTANT';

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
        const auth={
          password: this.state.password,
          mid: this.props.selectUser.mid
        }
        console.log("보낸다 가라아아앗",this.props.selectUser)
        console.log("보낸다 가라아아앗2",auth)

        if(auth.password==='') return alert("변경할 비밀번호를 입력하세요")
    
        return axios.post(`${BASE_URL}/api/users/modify`,
        {
          mem:this.props.selectUser,
          auth:auth
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