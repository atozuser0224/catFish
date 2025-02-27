# CatFish
## Develop by babynako & Dominicus0830

## GRADLE

## 리포지토리 - Repository
```gradle
maven { 
    url = 'maven { url 'https://jitpack.io' }'
}
```

## 디펜던시 - Dependencies
```gradle
implementation 'com.github.Dominicus0830:catFish:0.0.0.10'
```

## API
### ConfigUtils
```java
public static YamlConfiguration config;
public static TestPlugin plugin;
public void onEnable() {
    plugin = this;
    config = ConfigUtils.loadDefaultPluginConfig(plugin);
}
```

### ColorUtils
```java
String s = "&aTest String"
s = ColorUtils.applyColor(s);
String s2 = "<#FFFFFF>TestString"
s2 = ColorUtils.applyColor(s2);
```

### InventoryUtils
```java
Player p = Bukkit.getPlayer("PlayerName");
ItemStack item = new ItemStack(Material.COD, 10);
if(InventoryUtils.hasEnoughSpace(p.getInventory().getStorageContents(), item)) {
    p.getInventory().addItem(item);
}else{
    p.sendMessage("인벤토리에 남은 공간이 없습니다!");
}
```

### DataUtils 
##### MainClass
```
public class APITest extends JavaPlugin {
    private static APITest plugin;
    public static DataUtils data;
    public static APITest getInstance() {
        return plugin;
    }

    @Override
    public void onEnable() {
        plugin = this;
        data = new DataUtils(this);
    }
    ...
}
```
##### FunctionClass
```java
public class APIFunction {
    private static final APITest plugin = APITest.getInstance();
    private static final DataUtils data = plugin.data;

    public static void getItem(Player player, Inventory inv) {
        ItemStack stack = inv.getItem(1);
        data.getConfig().set("test", stack);
    }
    
    public static void setItem(Player player, Inventory inv) {
        ItemStack stack = data.getConfig().getItemStack("test");
        inv.setItem(1, stack);
    }

    public static void ifItem(Player player, Inventory inv) {
        if(data.getConfig().getItemStack("test") == null) {
            System.out.println("null");
            return;
        }else {
            System.out.println("not null");
        }
    }
}
```

### InventoryAPI
```java
InventoryAPI inv = new InventoryAPI(null, "Inventory Name", 54, true, plugin);
// InventoryHolder, Title Size, UsePage, Plugin
ItemStack COD = new ItemStack(Material.COD);

ItemStack prev = NBT.setStringTag(new ItemStack(Material.DIAMOND), "prev", "true");
ItemMeta im = prev.getItemMeta();
im.setDisplayName("이전 페이지");
prev.setItemMeta(im);
//setting prev item

ItemStack next = NBT.setStringTag(new ItemStack(Material.IRON_INGOT), "next", "true");
im = next.getItemMeta();
im.setDisplayName("다음 페이지");
next.setItemMeta(im);
//setting next item

inv.setPageTools(new ItemStack[]{COD, COD, prev, COD, COD, COD, next, COD, COD});
//must be 9 items
inv.addPageContent(/*ItemStack[]*/);
inv.update();
Player#openInventory(inv);
```
#### Setting Inventory Click Event
```java
@EventHandler
public void onInventoryClick(InventoryClickEvent e) {
    if (e.getClickedInventory() instanceof InventoryAPI) {
        InventoryAPI inv = (InventoryAPI) e.getClickedInventory();
        if (NBT.hasTagKey(e.getCurrentItem(), "prev")) {
            e.setCancelled(true);
            inv.prevPage();
            return;
        }
        if (NBT.hasTagKey(e.getCurrentItem(), "next")) {
            e.setCancelled(true);
            inv.nextPage();
            return;
        }
    }
}
```

#### MessageComponent
```java
public CatFish cf;
    public MessageComponent component;

    public void setUpCatFish() {
        if(Bukkit.getPluginManager().isPluginEnabled("CatFish")) {
            cf = (CatFish) Bukkit.getPluginManager().getPlugin("CatFish");
        }
    }

    public void onEnable() {
        setUpCatFish();
        new CatFishBuilder(this).addComponents(ComponentType.MESSAGE).build();
        component = (MessageComponent) cf.getComponent(this, ComponentType.MESSAGE);
        component.addMessages("접두어", "&f[ &e냥 &f] ");
        component.addMessages("얌얌얌", Arrays.asList("고", "냥", "이", "팔딱팔딱"));
        component.load();
        System.out.println(component.getString("접두어"));
        System.out.println(component.getList("얌얌얌"));
    }
}
```

#### PageInventoryComponent
```java
public CatFish cf;
public PageInventoryComponent component;

public void setUpCatFish() {
    if(Bukkit.getPluginManager().isPluginEnabled("CatFish")) {
           cf = (CatFish) Bukkit.getPluginManager().getPlugin("CatFish");
    }
}

public void onEnable() {
    setUpCatFish();
    new CatFishBuilder(this).addComponents(ComponentType.PAGEINVENTORY).build();
    component = (PageInventoryComponent) cf.getComponent(this, ComponentType.PAGEINVENTORY);
    component.setPageInventoryClass(CustomInventory.class);
    component.load();
}

@Override
public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player p) {
        if(!component.isInventory(p.getName())) {
            component.createInventory(p.getName(), 54);
            component.getInventory(p.getName()).createpage(0);
        }
        component.getInventory(p.getName()).openInventory(p, 0);
    }
    return super.onCommand(sender, command, label, args);
}
```
CustomInventory.class
```java
public class CustomInventory extends PageInventory {

    public CustomInventory(String name, PageInventoryComponent component, Integer invsize) {
        super(name, component, invsize);
    }

    public CustomInventory(PageInventoryComponent component, YamlConfiguration config) {
        super(component, config);
    }

    @Override
    public String InventoryName() {
        return name+"의 창고";
    }

    @Override
    public void close(InventoryCloseEvent e, InventoryOpener opener) {
        super.close(e, opener);
        for(int a = 0;a<invsize;a++) {
            Inventory inv = e.getInventory();
            if(inv.getItem(a)!=null) {
                setItem(opener.page, a, inv.getItem(a));
            }else {
                setItem(opener.page, a, new ItemStack(Material.AIR));
            }
        }
    }
}
```

## 인벤토리 API

#### 인벤토리 예제
```java
   RyseInventory.builder()
        .title("Delay Preview")
        .rows(3)
        .delay(3, TimeSetting.SECONDS)
        .provider(new InventoryProvider() {
            @Override
            public void update(Player player, InventoryContents contents) {
                    Material material = Material.values()[new Random().nextInt(Material.values().length)];
                    contents.update(0, new ItemStack(material));
            }
            @Override
            public void init(Player player, InventoryContents contents) {
                    contents.set(0, new ItemStack(Material.DIAMOND_SWORD));
            }
        })    
        .build(this)
        .openAll();
```

#### 아이템 생성 예
```java
contents.set(0, new ItemStack(Material.DIAMOND_SWORD));
```

#### 아이템 클릭 이벤트
```java
contents.set(8, IntelligentItem.of(new ItemStack(), event -> {
    your code
}));
```

#### 인벤토리 클릭 이벤트
```java
RyseInventory.builder()
                .title("listener preview")
                .rows(3)
                .listener(new EventCreator<>(InventoryClickEvent.class, event -> {
                    if(event.getClickedInventory() == null) return;

                    if(event.getSlot() == 1)
                        event.getClickedInventory().setItem(5, new ItemStack(Material.DIAMOND));
                }))
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {
                    }
                })
                .build(this)
                .openAll();
```
#### 인벤토리 페이지 예제
```java
RyseInventory.builder()
                .title("fixedPageSize preview")
                .rows(3)
                .fixedPageSize(5)
                .provider(new InventoryProvider() {
                    @Override
                    public void init(Player player, InventoryContents contents) {
                        Pagination pagination = contents.pagination();
                        contents.set(0, IntelligentItem.of(new ItemStack(Material.ARROW), event -> {
                            if (pagination.isFirst()) {
                                player.sendMessage("You are on the first page");
                                return;
                            }

                            RyseInventory currentInventory = pagination.inventory();
                            currentInventory.open(player, pagination.previous().page());
                        }));

                        contents.set(4, new ItemBuilder(getRandomMaterial()).displayName("TEST - " + getRandomNumber(1, Integer.MAX_VALUE - 1)).build());

                        contents.set(8, IntelligentItem.of(new ItemStack(Material.ARROW), event -> {
                            if (pagination.isLast()) {
                                player.sendMessage("You are on the last page");
                                return;
                            }

                            RyseInventory currentInventory = pagination.inventory();
                            currentInventory.open(player, pagination.next().page());
                        }));
                    }
                })
                .build(this)
                .openAll();
```

[인벤토리 API에 대한 자세한 정보](https://wiki.ryseinventory.de/classes/class-methods/ryseinventory/full-examples)