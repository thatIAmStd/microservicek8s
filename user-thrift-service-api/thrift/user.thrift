namespace java com.hydeng.thrift.user

struct UserInfo{
    1:i32 age,
    2:string username,
    3:string password,
    4:string realName,
    5:string mobile,
    6:string email
}

service UserService{

    UserInfo getUserById(1:string id);

    UserInfo getUserByName(1:string username);

    void registerUser(1:UserInfo userInfo);
}
