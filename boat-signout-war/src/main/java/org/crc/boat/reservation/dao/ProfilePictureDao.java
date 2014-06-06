package org.crc.boat.reservation.dao;


import static com.googlecode.objectify.ObjectifyService.ofy;

import java.util.logging.Logger;

import org.crc.boat.reservation.model.ProfilePicture;

import com.googlecode.objectify.ObjectifyService;

public class ProfilePictureDao {
	static final Logger LOG = Logger.getLogger(ProfilePictureDao.class.getName());
	static {
        ObjectifyService.register(ProfilePicture.class);
    }

	public ProfilePicture saveProfilePicture(ProfilePicture profilePicture) {

	    ProfilePicture existingPic = getProfilePictureByName(profilePicture.getUserName());
	    if(existingPic == null){
	        LOG.info("Inserting profilepicture " + profilePicture.getUserName());
	    }else{
            LOG.info("Updating profilepicture " + profilePicture.getUserName());
	        profilePicture.setId(existingPic.getId());
	    }
	    ofy().save().entity(profilePicture).now();
		return profilePicture;
		
	}

	public void deleteProfilePicture(String userName) {

		LOG.info("Deleting profilepicture " + userName);
		ProfilePicture picToDelete = getProfilePictureByName(userName);
		if(picToDelete != null){
		    ofy().delete().entity(picToDelete);
		}
		
	}

	public ProfilePicture getProfilePictureByName(String userName){
		LOG.info("get profilepicture for " + userName);
		ProfilePicture pic = ofy().load().type(ProfilePicture.class).filter("userName", userName).first().now();
		if(pic == null ){
		    LOG.info("Profile picture not found for user " + userName);
		    return null;
		}else{
            LOG.info("Profile picture found for user " + userName + " with size " + (pic.getImage() == null ? null :pic.getImage().length));		    
		}
		return pic;
		
	}
	
	public ProfilePicture getProfilePictureById(Long id){
        LOG.info("get profilepicture for id " + id);
        ProfilePicture pic = ofy().load().type(ProfilePicture.class).id(id).now();
        if(pic == null ){
            LOG.info("Profile picture not found for id " + id);
            return null;
        }else{
            LOG.info("Profile picture found for id " + id + " with size " + (pic.getImage() == null ? null :pic.getImage().length));            
        }
        return pic;
        
    }
}
