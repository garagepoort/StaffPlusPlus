# Staff++
Forked from QBall StaffPlus github's repository.

I forked this repository to implement a release cycle strategy in which different versions for different server will be supported.
The main reason for this is so that we can start implementing new features easily using new versions of the spigot api, without worrying about backwards compatibility.

##Versions
With this new release cycle a new versioning strategy is introduced:

For every spigot version we will create a new release. This means that you must download not the newest update but the one suitable for your server.
Example:

- Server on Spigot 1.8.x -> StaffPlusPlus 1.8.x
- Server on Spigot 1.14.x -> StaffPlusPlus 1.14.x
- Server on Spigot 1.16.x -> StaffPlusPlus 1.16.x

##Support 
- New features will be added to all versions >= 1.15
- Bugs will be fixed on all versions  >= 1.11
- End Of Life, no support will be provided for versions < 1.11

----
#### Useful links
* [Resource page](https://www.spigotmc.org/resources/staffplusplus.83562/)
* [Submit issues](https://github.com/garagepoort/StaffPlusPlus/issues)
* [Request features or ask questions](https://discord.gg/N6VqtYC)
* [Configurable files](https://github.com/Shortninja66/StaffPlus/wiki/Configurable-files)
* [Plugin jar download](https://www.spigotmc.org/resources/staffplusplus.83562/history)
* [Help wiki](https://github.com/Shortninja66/StaffPlus/wiki)

#### Contributing
* Fork this repo
* Clone your repo
* Make your changes
* Submit a pull request

#### Building
* Clone this repo
* Run mvn clean package
* Wait (if you have never ran BuildTools before it will take a while future builds will be faster)