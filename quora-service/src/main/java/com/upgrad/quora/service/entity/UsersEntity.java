package com.upgrad.quora.service.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "users" , schema = "quora")
public class UsersEntity implements Serializable {

}
