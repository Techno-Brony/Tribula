package net.minecraft.server;

import com.google.common.base.Function;
import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;

public abstract class ChatBaseComponent implements IChatBaseComponent {

    protected List<IChatBaseComponent> a = Lists.newArrayList();
    private ChatModifier b;

    @SuppressWarnings("unused")
    public ChatBaseComponent() {}

    public static Iterator<IChatBaseComponent> a(Iterable<IChatBaseComponent> iterable) {
        //noinspection unchecked
        Iterator iterator = Iterators.concat(Iterators.transform(iterable.iterator(), new Function() {
            public Iterator<IChatBaseComponent> a(@Nullable IChatBaseComponent ichatbasecomponent) {
                return ichatbasecomponent.iterator();
            }

            public Object apply(Object object) {
                return this.a((IChatBaseComponent) object);
            }
        }));

        iterator = Iterators.transform(iterator, new Function() {
            public IChatBaseComponent a(@Nullable IChatBaseComponent ichatbasecomponent) {
                IChatBaseComponent ichatbasecomponent1 = ichatbasecomponent.f();

                ichatbasecomponent1.setChatModifier(ichatbasecomponent1.getChatModifier().n());
                return ichatbasecomponent1;
            }

            public Object apply(Object object) {
                return this.a((IChatBaseComponent) object);
            }
        });
        //noinspection unchecked
        return iterator;
    }

    public IChatBaseComponent addSibling(IChatBaseComponent ichatbasecomponent) {
        ichatbasecomponent.getChatModifier().setChatModifier(this.getChatModifier());
        this.a.add(ichatbasecomponent);
        return this;
    }

    public List<IChatBaseComponent> a() {
        return this.a;
    }

    public IChatBaseComponent a(String s) {
        return this.addSibling(new ChatComponentText(s));
    }

    public IChatBaseComponent setChatModifier(ChatModifier chatmodifier) {
        this.b = chatmodifier;
        Iterator iterator = this.a.iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

            ichatbasecomponent.getChatModifier().setChatModifier(this.getChatModifier());
        }

        return this;
    }

    public ChatModifier getChatModifier() {
        if (this.b == null) {
            this.b = new ChatModifier();
            Iterator iterator = this.a.iterator();

            //noinspection WhileLoopReplaceableByForEach
            while (iterator.hasNext()) {
                IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

                ichatbasecomponent.getChatModifier().setChatModifier(this.b);
            }
        }

        return this.b;
    }

    public Iterator<IChatBaseComponent> iterator() {
        //noinspection unchecked,unchecked
        return Iterators.concat(Iterators.forArray(this), a((Iterable) this.a));
    }

    public final String toPlainText() {
        StringBuilder stringbuilder = new StringBuilder();
        Iterator iterator = this.iterator();

        //noinspection WhileLoopReplaceableByForEach
        while (iterator.hasNext()) {
            IChatBaseComponent ichatbasecomponent = (IChatBaseComponent) iterator.next();

            stringbuilder.append(ichatbasecomponent.getText());
        }

        return stringbuilder.toString();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof ChatBaseComponent)) {
            return false;
        } else {
            ChatBaseComponent chatbasecomponent = (ChatBaseComponent) object;

            return this.a.equals(chatbasecomponent.a) && this.getChatModifier().equals(chatbasecomponent.getChatModifier());
        }
    }

    public int hashCode() {
        return 31 * this.getChatModifier().hashCode() + this.a.hashCode(); // CraftBukkit - fix null pointer
    }

    public String toString() {
        return "BaseComponent{style=" + this.b + ", siblings=" + this.a + '}';
    }
}
