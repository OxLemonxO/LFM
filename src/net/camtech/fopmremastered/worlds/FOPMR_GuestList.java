package net.camtech.fopmremastered.worlds;

import java.util.HashMap;

public class FOPMR_GuestList
{
    public HashMap<String, String> guests;
    
    public FOPMR_GuestList()
    {
        guests = new HashMap<>();
    }
    
    public boolean isGuest(String name)
    {
        return guests.containsKey(name);
    }
    
    public void addGuest(String guest, String moderator)
    {
        guests.put(guest, moderator);
    }
    
    public void removeGuest(String guest)
    {
        guests.remove(guest);
    }
    
    public void removeGuestsFromModerator(String moderator)
    {
        for(String guest : guests.keySet())
        {
            if(guests.get(guest).equals(moderator))
            {
                removeGuest(guest);
            }
        }
    }
}
