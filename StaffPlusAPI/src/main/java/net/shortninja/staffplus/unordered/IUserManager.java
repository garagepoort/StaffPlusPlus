package net.shortninja.staffplus.unordered;

import java.util.Collection;
import java.util.UUID;

public interface IUserManager {

	Collection<IUser> getAll();

	IUser get(UUID id);
}
