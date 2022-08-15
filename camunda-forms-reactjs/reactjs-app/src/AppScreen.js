import React, { Component } from 'react';

class AppScreen extends Component {

    doLogout = () =>  (e, results) => {this.props.onlogout()}

    render() {
        return (
          <div>
            <nav class="navbar" role="navigation" aria-label="main navigation">
              <div class="navbar-brand">
                <a className="navbar-item" href="https://docs.camunda.io">
                  <img src="/Logo_Black.png" height="28" alt={"logo"}/>
                </a>
                {// eslint-disable-next-line
                <a role="button" className="navbar-burger" aria-label="menu" aria-expanded="false"
                   data-target="navbarBasicExample">
                  <span aria-hidden="true"></span>
                  <span aria-hidden="true"></span>
                  <span aria-hidden="true"></span>
                </a>}
              </div>

              <div id="navbarBasicExample" class="navbar-menu">
                <div class="navbar-start">
                </div>

                <div class="navbar-end">
                  {this.props.userId ?
                    <React.Fragment>
                    <div class="navbar-item">
                      <span>Welcome {this.props.userId}</span>
                    </div>
                    <div class="navbar-item">
                      <button className={"button"} onClick={this.doLogout()}>Logout</button>
                    </div>
                    </React.Fragment>
                    : null
                  }
                  <div class="navbar-item">
                    <a href="https://github.com/upgradingdave/camunda-deep-dives/camunda-forms-reactjs"
                       target="_source"><i class="fa fa-external-link"></i> Source Code</a>
                  </div>
                </div>
              </div>
            </nav>
            <section className="section">
              <div className="container">
                {this.props.children}
              </div>
            </section>

          </div>
        );
    }
}

export default AppScreen;
