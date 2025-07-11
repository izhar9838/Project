package sm.central.service.user;

import java.util.List;

import sm.central.dto.user.ChangePasswordDto;
import sm.central.dto.user.UpdateUserDto;
import sm.central.dto.user.UserInfoDto;
import sm.central.model.content.Announcement;
import sm.central.model.content.BlogPost;
import sm.central.model.content.HallOfFameEntity;
import sm.central.model.content.Timetable;
import sm.central.security.model.UserEntity;

public interface IUserService {
	public List<Timetable> getAllTimetables();
	public List<HallOfFameEntity> getAllHallOfFame();
	public List<BlogPost> getAllBlogPost();
	public BlogPost getBlog(Long id);
	public UserInfoDto getUserInfo(String username,String role);
	public UserEntity updateProfile(UpdateUserDto userDto,String username);
	public String changePassword(ChangePasswordDto changeDto,String username);
	public List<Announcement> getAnnouncements();
	public UserEntity findByUsername(String username);
	
}
