package org.primefaces.sapphire.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GuestPreferences implements Serializable {

    private String layout = "blue";

    private String theme = "blue";

    private String menuTheme = "light";

    private String topBar = "blue";
    
    private String topBarSize = "large";

    private boolean horizontal = true;

    private List<PrimaryColor> primaryColors = new ArrayList<PrimaryColor>();

    private List<TopbarTheme> topbarThemes = new ArrayList<TopbarTheme>();

    private List<MenuTheme> menuThemes = new ArrayList<MenuTheme>();

    private List<ComponentTheme> componentThemes = new ArrayList<ComponentTheme>();
    
    private List<String> topbarSizes = new ArrayList<String>();

    @PostConstruct
    public void init() {
        primaryColors.add(new PrimaryColor("Amber", "amber", "#FFC107"));
        primaryColors.add(new PrimaryColor("Blue", "blue", "#457fca"));
        primaryColors.add(new PrimaryColor("BlueGray", "bluegray", "#607D8B"));
        primaryColors.add(new PrimaryColor("Brown", "brown", "#795548"));
        primaryColors.add(new PrimaryColor("Cyan", "cyan", "#00ACC1"));
        primaryColors.add(new PrimaryColor("DeepOrange", "deeporange", "#FF5722"));
        primaryColors.add(new PrimaryColor("DeepPurple", "deeppurple", "#673AB7"));
        primaryColors.add(new PrimaryColor("Gray", "gray", "#757575"));
        primaryColors.add(new PrimaryColor("Green", "green", "#4CAF50"));
        primaryColors.add(new PrimaryColor("Indigo", "indigo", "#3F51B5"));
        primaryColors.add(new PrimaryColor("LightBlue", "lightblue", "#03A9F4"));
        primaryColors.add(new PrimaryColor("LightGreen", "lightgreen", "#8BC34A"));
        primaryColors.add(new PrimaryColor("Lime", "lime", "#C0CA33"));
        primaryColors.add(new PrimaryColor("Orange", "orange", "#FF9800"));
        primaryColors.add(new PrimaryColor("Pink", "pink", "#E91E63"));
        primaryColors.add(new PrimaryColor("Purple", "purple", "#9C27B0"));
        primaryColors.add(new PrimaryColor("Teal", "teal", "#009688"));
        primaryColors.add(new PrimaryColor("Yellow", "yellow", "#FDD835"));

        topbarThemes.add(new TopbarTheme("Blue", "blue", "blue.svg"));
        topbarThemes.add(new TopbarTheme("Ash", "ash", "ash.svg"));
        topbarThemes.add(new TopbarTheme("Kashmir", "kashmir", "kashmir.svg"));
        topbarThemes.add(new TopbarTheme("Orange", "orange", "orange.svg"));
        topbarThemes.add(new TopbarTheme("Midnight", "midnight", "midnight.svg"));
        topbarThemes.add(new TopbarTheme("Sunset", "sunset", "sunset.svg"));
        topbarThemes.add(new TopbarTheme("Marley", "marley", "marley.svg"));
        topbarThemes.add(new TopbarTheme("Harvey", "harvey", "harvey.svg"));
        topbarThemes.add(new TopbarTheme("Vanusa", "vanusa", "vanusa.svg"));
        topbarThemes.add(new TopbarTheme("Skyline", "skyline", "skyline.svg"));
        topbarThemes.add(new TopbarTheme("Royal", "royal", "royal.svg"));
        topbarThemes.add(new TopbarTheme("Disco", "disco", "disco.svg"));
        topbarThemes.add(new TopbarTheme("Sky", "sky", "sky.svg"));
        topbarThemes.add(new TopbarTheme("Rose", "rose", "rose.svg"));
        topbarThemes.add(new TopbarTheme("Revolt", "revolt", "revolt.svg"));
        topbarThemes.add(new TopbarTheme("Forest", "forest", "forest.svg"));
        topbarThemes.add(new TopbarTheme("Night", "night", "night.svg"));
        topbarThemes.add(new TopbarTheme("Apricot", "apricot", "apricot.svg"));
        topbarThemes.add(new TopbarTheme("Faraway", "faraway", "faraway.svg"));
        topbarThemes.add(new TopbarTheme("Grape", "grape", "grape.svg"));
        topbarThemes.add(new TopbarTheme("River", "river", "river.svg"));
        topbarThemes.add(new TopbarTheme("Dock", "dock", "dock.svg"));
        topbarThemes.add(new TopbarTheme("Material One", "materialone", "materialone.png"));
        topbarThemes.add(new TopbarTheme("Material Two", "materialtwo", "materialtwo.png"));
        topbarThemes.add(new TopbarTheme("Polygons", "polygons", "polygons.png"));
        topbarThemes.add(new TopbarTheme("Connections One", "connectionsone", "connectionsone.png"));
        topbarThemes.add(new TopbarTheme("Connections Two", "connectionstwo", "connectionstwo.png"));
        topbarThemes.add(new TopbarTheme("Road", "road", "road.png"));
        topbarThemes.add(new TopbarTheme("Reflection", "reflection", "reflection.png"));
        topbarThemes.add(new TopbarTheme("Waves", "waves", "waves.png"));
        topbarThemes.add(new TopbarTheme("Sandiego", "sandiego", "sandiego.png"));
        topbarThemes.add(new TopbarTheme("Architecture", "architecture", "architecture.png"));
        topbarThemes.add(new TopbarTheme("Snow", "snow", "snow.png"));
        topbarThemes.add(new TopbarTheme("Palm", "palm", "palm.png"));
        topbarThemes.add(new TopbarTheme("Fluid", "fluid", "fluid.png"));
        topbarThemes.add(new TopbarTheme("Balloon", "balloon", "balloon.png"));
        topbarThemes.add(new TopbarTheme("Downtown", "downtown", "downtown.png"));
        topbarThemes.add(new TopbarTheme("Perfection", "perfection", "perfection.png"));
        topbarThemes.add(new TopbarTheme("Northern", "northern", "northern.png"));
        topbarThemes.add(new TopbarTheme("Highline", "highline", "highline.png"));
        topbarThemes.add(new TopbarTheme("Mural", "mural", "mural.png"));
        topbarThemes.add(new TopbarTheme("Aeriel", "aeriel", "aeriel.png"));
        topbarThemes.add(new TopbarTheme("Wing", "wing", "wing.png"));
        topbarThemes.add(new TopbarTheme("Skyscraper", "skyscraper", "skyscraper.png"));
        topbarThemes.add(new TopbarTheme("Wall", "wall", "wall.png"));
        topbarThemes.add(new TopbarTheme("Dawn", "dawn", "dawn.png"));
        topbarThemes.add(new TopbarTheme("Lille", "lille", "lille.png"));
        topbarThemes.add(new TopbarTheme("Condo", "condo", "condo.png"));
        topbarThemes.add(new TopbarTheme("Waterfall", "waterfall", "waterfall.png"));
        topbarThemes.add(new TopbarTheme("Coffee", "coffee", "coffee.png"));
        topbarThemes.add(new TopbarTheme("Mountain", "mountain", "mountain.png"));
        topbarThemes.add(new TopbarTheme("Lights", "lights", "lights.png"));
        topbarThemes.add(new TopbarTheme("Desert", "desert", "desert.png"));
        topbarThemes.add(new TopbarTheme("Beach", "beach", "beach.png"));
        topbarThemes.add(new TopbarTheme("Classic", "classic", "classic.png"));
        topbarThemes.add(new TopbarTheme("Hazy", "hazy", "hazy.png"));
        topbarThemes.add(new TopbarTheme("Exposure", "exposure", "exposure.png"));
        topbarThemes.add(new TopbarTheme("Norge", "norge", "norge.png"));
        topbarThemes.add(new TopbarTheme("Island", "island", "island.png"));
        topbarThemes.add(new TopbarTheme("Station", "station", "station.png"));
        topbarThemes.add(new TopbarTheme("Fruity", "fruity", "fruity.png"));
        topbarThemes.add(new TopbarTheme("Tropical", "tropical", "tropical.png"));
        topbarThemes.add(new TopbarTheme("Beyoglu", "beyoglu", "beyoglu.png"));
        topbarThemes.add(new TopbarTheme("Timelapse", "timelapse", "timelapse.png"));
        topbarThemes.add(new TopbarTheme("Crystal", "crystal", "crystal.png"));
        topbarThemes.add(new TopbarTheme("Aquarelle", "aquarelle", "aquarelle.png"));
        topbarThemes.add(new TopbarTheme("Canvas", "canvas", "canvas.png"));
        topbarThemes.add(new TopbarTheme("Olympic", "olympic", "olympic.png"));
        topbarThemes.add(new TopbarTheme("Circuit", "circuit", "circuit.png"));
        topbarThemes.add(new TopbarTheme("Flamingo", "flamingo", "flamingo.png"));
        topbarThemes.add(new TopbarTheme("Flight", "flight", "flight.png"));
        topbarThemes.add(new TopbarTheme("Tractor", "tractor", "tractor.png"));
        topbarThemes.add(new TopbarTheme("Volcano", "volcano", "volcano.png"));
        topbarThemes.add(new TopbarTheme("Pine", "pine", "pine.png"));
        topbarThemes.add(new TopbarTheme("Emptiness", "emptiness", "emptiness.png"));
        topbarThemes.add(new TopbarTheme("Splash", "splash", "splash.png"));
        topbarThemes.add(new TopbarTheme("Urban", "urban", "urban.png"));
        topbarThemes.add(new TopbarTheme("Bloom", "bloom", "bloom.png"));
        topbarThemes.add(new TopbarTheme("Tinfoil", "tinfoil", "tinfoil.png"));
        topbarThemes.add(new TopbarTheme("Hallway", "hallway", "hallway.png"));
        topbarThemes.add(new TopbarTheme("Seagull", "seagull", "seagull.png"));
        topbarThemes.add(new TopbarTheme("City", "city", "city.png"));
        topbarThemes.add(new TopbarTheme("Jet", "jet", "jet.png"));
        topbarThemes.add(new TopbarTheme("Louisville", "louisville", "louisville.png"));
        topbarThemes.add(new TopbarTheme("Spray", "spray", "spray.png"));
        topbarThemes.add(new TopbarTheme("Symmetry", "symmetry", "symmetry.png"));
        topbarThemes.add(new TopbarTheme("Destination", "destination", "destination.png"));

        menuThemes.add(new MenuTheme("Light", "light", "light.svg"));
        menuThemes.add(new MenuTheme("Dark", "dark", "dark.svg"));
        menuThemes.add(new MenuTheme("Blue", "blue", "blue.svg"));
        menuThemes.add(new MenuTheme("Ash", "ash", "ash.svg"));
        menuThemes.add(new MenuTheme("Kashmir", "kashmir", "kashmir.svg"));
        menuThemes.add(new MenuTheme("Orange", "orange", "orange.svg"));
        menuThemes.add(new MenuTheme("Midnight", "midnight", "midnight.svg"));
        menuThemes.add(new MenuTheme("Sunset", "sunset", "sunset.svg"));
        menuThemes.add(new MenuTheme("Marley", "marley", "marley.svg"));
        menuThemes.add(new MenuTheme("Harvey", "harvey", "harvey.svg"));
        menuThemes.add(new MenuTheme("Vanusa", "vanusa", "vanusa.svg"));
        menuThemes.add(new MenuTheme("Skyline", "skyline", "skyline.svg"));
        menuThemes.add(new MenuTheme("Royal", "royal", "royal.svg"));
        menuThemes.add(new MenuTheme("Disco", "disco", "disco.svg"));
        menuThemes.add(new MenuTheme("Sky", "sky", "sky.svg"));
        menuThemes.add(new MenuTheme("Rose", "rose", "rose.svg"));
        menuThemes.add(new MenuTheme("Revolt", "revolt", "revolt.svg"));
        menuThemes.add(new MenuTheme("Forest", "forest", "forest.svg"));
        menuThemes.add(new MenuTheme("Night", "night", "night.svg"));
        menuThemes.add(new MenuTheme("Apricot", "apricot", "apricot.svg"));
        menuThemes.add(new MenuTheme("Faraway", "faraway", "faraway.svg"));
        menuThemes.add(new MenuTheme("Grape", "grape", "grape.svg"));
        menuThemes.add(new MenuTheme("River", "river", "river.svg"));
        menuThemes.add(new MenuTheme("Dock", "dock", "dock.svg"));
        menuThemes.add(new MenuTheme("Material One", "materialone", "materialone.png"));
        menuThemes.add(new MenuTheme("Material Two", "materialtwo", "materialtwo.png"));
        menuThemes.add(new MenuTheme("Polygon", "polygons", "polygons.png"));
        menuThemes.add(new MenuTheme("Connection One", "connectionsone", "connectionsone.png"));
        menuThemes.add(new MenuTheme("Connection Two", "connectionstwo", "connectionstwo.png"));
        menuThemes.add(new MenuTheme("Road", "road", "road.png"));
        menuThemes.add(new MenuTheme("Reflection", "reflection", "reflection.png"));
        menuThemes.add(new MenuTheme("Waves", "waves", "waves.png"));
        menuThemes.add(new MenuTheme("Sandiego", "sandiego", "sandiego.png"));
        menuThemes.add(new MenuTheme("Architecture", "architecture", "architecture.png"));
        menuThemes.add(new MenuTheme("Snow", "snow", "snow.png"));
        menuThemes.add(new MenuTheme("Palm", "palm", "palm.png"));
        menuThemes.add(new MenuTheme("Fluid", "fluid", "fluid.png"));
        menuThemes.add(new MenuTheme("Balloon", "balloon", "balloon.png"));
        menuThemes.add(new MenuTheme("Downtown", "downtown", "downtown.png"));
        menuThemes.add(new MenuTheme("Perfection", "perfection", "perfection.png"));
        menuThemes.add(new MenuTheme("Northern", "northern", "northern.png"));
        menuThemes.add(new MenuTheme("Highline", "highline", "highline.png"));
        menuThemes.add(new MenuTheme("Mural", "mural", "mural.png"));
        menuThemes.add(new MenuTheme("Aeriel", "aeriel", "aeriel.png"));
        menuThemes.add(new MenuTheme("Wing", "wing", "wing.png"));
        menuThemes.add(new MenuTheme("Skyscraper", "skyscraper", "skyscraper.png"));
        menuThemes.add(new MenuTheme("Wall", "wall", "wall.png"));
        menuThemes.add(new MenuTheme("Dawn", "dawn", "dawn.png"));
        menuThemes.add(new MenuTheme("Lille", "lille", "lille.png"));
        menuThemes.add(new MenuTheme("Condo", "condo", "condo.png"));
        menuThemes.add(new MenuTheme("Waterfall", "waterfall", "waterfall.png"));
        menuThemes.add(new MenuTheme("Coffee", "coffee", "coffee.png"));
        menuThemes.add(new MenuTheme("Mountain", "mountain", "mountain.png"));
        menuThemes.add(new MenuTheme("Lights", "lights", "lights.png"));
        menuThemes.add(new MenuTheme("Desert", "desert", "desert.png"));
        menuThemes.add(new MenuTheme("Beach", "beach", "beach.png"));
        menuThemes.add(new MenuTheme("Classic", "classic", "classic.png"));
        menuThemes.add(new MenuTheme("Hazy", "hazy", "hazy.png"));
        menuThemes.add(new MenuTheme("Exposure", "exposure", "exposure.png"));
        menuThemes.add(new MenuTheme("Norge", "norge", "norge.png"));
        menuThemes.add(new MenuTheme("Island", "island", "island.png"));
        menuThemes.add(new MenuTheme("Station", "station", "station.png"));
        menuThemes.add(new MenuTheme("Fruity", "fruity", "fruity.png"));
        menuThemes.add(new MenuTheme("Tropical", "tropical", "tropical.png"));
        menuThemes.add(new MenuTheme("Beyoglu", "beyoglu", "beyoglu.png"));
        menuThemes.add(new MenuTheme("Timelapse", "timelapse", "timelapse.png"));
        menuThemes.add(new MenuTheme("Crystal", "crystal", "crystal.png"));
        menuThemes.add(new MenuTheme("Aquarelle", "aquarelle", "aquarelle.png"));
        menuThemes.add(new MenuTheme("Canvas", "canvas", "canvas.png"));
        menuThemes.add(new MenuTheme("Olympic", "olympic", "olympic.png"));
        menuThemes.add(new MenuTheme("Circuit", "circuit", "circuit.png"));
        menuThemes.add(new MenuTheme("Flamingo", "flamingo", "flamingo.png"));
        menuThemes.add(new MenuTheme("Flight", "flight", "flight.png"));
        menuThemes.add(new MenuTheme("Tractor", "tractor", "tractor.png"));
        menuThemes.add(new MenuTheme("Volcano", "volcano", "volcano.png"));
        menuThemes.add(new MenuTheme("Pine", "pine", "pine.png"));
        menuThemes.add(new MenuTheme("Emptiness", "emptiness", "emptiness.png"));
        menuThemes.add(new MenuTheme("Splash", "splash", "splash.png"));
        menuThemes.add(new MenuTheme("Urban", "urban", "urban.png"));
        menuThemes.add(new MenuTheme("Bloom", "bloom", "bloom.png"));
        menuThemes.add(new MenuTheme("Tinfoil", "tinfoil", "tinfoil.png"));
        menuThemes.add(new MenuTheme("Hallway", "hallway", "hallway.png"));
        menuThemes.add(new MenuTheme("Seagull", "seagull", "seagull.png"));
        menuThemes.add(new MenuTheme("City", "city", "city.png"));
        menuThemes.add(new MenuTheme("Jet", "jet", "jet.png"));
        menuThemes.add(new MenuTheme("Louisville", "louisville", "louisville.png"));
        menuThemes.add(new MenuTheme("Spray", "spray", "spray.png"));
        menuThemes.add(new MenuTheme("Symmetry", "symmetry", "symmetry.png"));
        menuThemes.add(new MenuTheme("Destination", "destination", "destination.png"));

        componentThemes.add(new ComponentTheme("Amber", "amber", "amber.svg"));
        componentThemes.add(new ComponentTheme("Blue", "blue", "blue.svg"));
        componentThemes.add(new ComponentTheme("Bluegray", "bluegray", "bluegray.svg"));
        componentThemes.add(new ComponentTheme("Brown", "brown", "brown.svg"));
        componentThemes.add(new ComponentTheme("Cyan", "cyan", "cyan.svg"));
        componentThemes.add(new ComponentTheme("Deep Orange", "deeporange", "deeporange.svg"));
        componentThemes.add(new ComponentTheme("Deep Purple", "deeppurple", "deeppurple.svg"));
        componentThemes.add(new ComponentTheme("Gray", "gray", "gray.svg"));
        componentThemes.add(new ComponentTheme("Green", "green", "green.svg"));
        componentThemes.add(new ComponentTheme("Indigo", "indigo", "indigo.svg"));
        componentThemes.add(new ComponentTheme("Light Blue", "lightblue", "lightblue.svg"));
        componentThemes.add(new ComponentTheme("Light Green", "lightgreen", "lightgreen.svg"));
        componentThemes.add(new ComponentTheme("Lime", "lime", "lime.svg"));
        componentThemes.add(new ComponentTheme("Orange", "orange", "orange.svg"));
        componentThemes.add(new ComponentTheme("Pink", "pink", "pink.svg"));
        componentThemes.add(new ComponentTheme("Purple", "purple", "purple.svg"));
        componentThemes.add(new ComponentTheme("Teal", "teal", "teal.svg"));
        componentThemes.add(new ComponentTheme("Yellow", "yellow", "yellow.svg"));
        
        topbarSizes.add("large");
        topbarSizes.add("medium");
        topbarSizes.add("small");
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getLayout() {
        return layout;
    }

    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getMenuTheme() {
        return menuTheme;
    }

    public void setMenuTheme(String menuTheme) {
        this.menuTheme = menuTheme;
    }

    public String getTopBar() {
        return topBar;
    }

    public void setTopBar(String topBar) {
        this.topBar = topBar;
    }

    public String getTopBarSize() {
        return topBarSize;
    }

    public void setTopBarSize(String topBarSize) {
        this.topBarSize = topBarSize;
    }

    public boolean isHorizontal() {
        return this.horizontal;
    }

    public void setHorizontal(boolean value) {
        this.horizontal = value;
    }

    public List<PrimaryColor> getPrimaryColors() {
        return primaryColors;
    }

    public List<TopbarTheme> getTopbarThemes() {
        return topbarThemes;
    }

    public List<MenuTheme> getMenuThemes() {
        return menuThemes;
    }

    public List<ComponentTheme> getComponentThemes() {
        return componentThemes;
    }

    public List<String> getTopbarSizes() {
        return topbarSizes;
    }

    public void setTopbarSizes(List<String> topbarSizes) {
        this.topbarSizes = topbarSizes;
    }

    public class PrimaryColor {

        String name;
        String file;
        String color;

        public PrimaryColor(String name, String file, String color) {
            this.name = name;
            this.file = file;
            this.color = color;
        }

        public String getName() {
            return this.name;
        }

        public String getFile() {
            return this.file;
        }

        public String getColor() {
            return this.color;
        }
    }

    public class TopbarTheme {

        String name;
        String file;
        String image;

        public TopbarTheme(String name, String file, String image) {
            this.name = name;
            this.file = file;
            this.image = image;
        }

        public String getName() {
            return this.name;
        }

        public String getFile() {
            return this.file;
        }

        public String getImage() {
            return this.image;
        }
    }

    public class MenuTheme {

        String name;
        String file;
        String image;

        public MenuTheme(String name, String file, String image) {
            this.name = name;
            this.file = file;
            this.image = image;
        }

        public String getName() {
            return this.name;
        }

        public String getFile() {
            return this.file;
        }

        public String getImage() {
            return this.image;
        }
    }

    public class ComponentTheme {

        String name;
        String file;
        String image;

        public ComponentTheme(String name, String file, String image) {
            this.name = name;
            this.file = file;
            this.image = image;
        }

        public String getName() {
            return this.name;
        }

        public String getFile() {
            return this.file;
        }

        public String getImage() {
            return this.image;
        }
    }
}
