# ministry

## Mutation

## Ministry

query getminitriy {
  getAllMinistries {
    ministryCode
    ministryName
    
  }
}

mutation addMinistry {
  addMinistry(
    newMinistryCode : 15000
    newMinistryName : "Test Ministry"
  ) {
    ministryCode
    ministryName
  }
}

mutation editMinistry {
  editMinistry(
    ministryCode : 15000
    newMinistryCode : 16000
  ) {
    ministryCode
    ministryName
  }
}

mutation editMinistry {
  editMinistry(
    ministryCode : 16000
    newMinistryCode : 16000
    newMinistryName : "Test Edit Ministry Name"
  ) {
    ministryCode
    ministryName
  }
}

mutation delMin {
  deleteMinistry(
    ministryCode :16000
  )
}

## Department

query getdep {
  getAllDepartments {
    ministryCode
    ministryName
    departmentCode
    departmentName
    
    
  }
}

mutation addDep {
  addDepartment(
    ministryCode : 15000
    newDepartmentCode : 15001
    newDepartmentName : "Dep 150001"
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
    }
  }
}

mutation editDep {
  editDepartment(
    newMinistryCode :15000
    currentDeptCode : 15001
    newDeptCode : 15002
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
    }
  }
}

mutation editDep {
  editDepartment(
    newMinistryCode :15000
    currentDeptCode : 15002
    newDeptCode : 15001
    newDeptName : "Dep 15002"
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
    }
  }
}

## Moving to Another Ministry
mutation delDep {
  deleteDepartment(
    departmentCode : 15002
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
    }
  }
}
## Organisation
query getAllOrgs {
  getAllOrganisations {
    organisationCode
    organisationName
    departmentCode
    departmentName
  }
}

mutation addOrg {
  addOrganisation(
    departmentCode : 14655
    newOrganisationCode : 1465510
    newOrganisationName : "new 1465510"
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
      organisations {
        orgName
        orgCode
        posts {
          postCode
          postName
          genderCode
        }
      }
    }
  }
}

mutation editOrg {
  editOrganisation (
    currentOrgCode : 15001002
    newOrgCode : 150010021
    newOrgName : "Org2 edit Dept 15001"
    newDeptCode : 15001
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
      organisations {
        orgCode
        orgName
        posts {
          postCode
          postName
          genderCode
        }
      }
    }
  }
}

mutation editOrg {
  editOrganisation (
    currentOrgCode : 15001001
    newOrgCode : 15001001
    newOrgName : "Org1 for Dept 15001"
    newDeptCode : 15002
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
      organisations {
        orgCode
        orgName
        posts {
          postCode
          postName
          genderCode
        }
      }
    }
  }
}


## Post
query getPosts {
  getAllPosts {
    postsCode
    postsName
    gendersCode
    organisationCode
    organisationName
    departmentCode
    departmentName
    ministryCode
    ministryName

  }
}

mutation addPost {
  addPost(
    organisationCode : 1465510
    newPostCode : 14655101
    newPostName : "Post 14655101"
    newGenderCode : 1
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
      organisations {
        orgName
        orgCode
        posts {
          postCode
          postName
          genderCode
        }
      }
    }
  }
}

mutation editPost {
  deletePost(
    postCode : 14655101
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
      organisations {
        orgName
        orgCode
        posts {
          postCode
          postName
          genderCode
        }
      }
    }
  }
}

mutation editPostNew {
  editPost(
    newOrgCode : 146569
    currentPostCode : 14655101
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
      organisations {
        orgName
        orgCode
        posts {
          postCode
          postName
          genderCode
        }
      }
    }
  }
}

mutation editPostNew {
  editPost(
    newOrgCode : 146569
    currentPostCode : 14655101
    newPostCode : 14655102
    newPostName : "Edited Post Name"
    newGenderCode : 2
  ) {
    ministryCode
    ministryName
    departments {
      deptCode
      deptName
      organisations {
        orgName
        orgCode
        posts {
          postCode
          postName
          genderCode
        }
      }
    }
  }
}
