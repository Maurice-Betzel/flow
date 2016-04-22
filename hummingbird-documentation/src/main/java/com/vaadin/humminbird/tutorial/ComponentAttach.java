package com.vaadin.humminbird.tutorial;

import java.util.EventObject;
import java.util.function.Consumer;

import com.vaadin.annotations.Tag;
import com.vaadin.humminbird.tutorial.annotations.CodeFor;
import com.vaadin.ui.Component;

@CodeFor("tutorial-component-with-dependencies.asciidoc")
public class ComponentAttach {

    interface I18N {
        static String getTranslation(String localeName, String key,
                String... params) {
            return "";
        }
    }

    interface ShopEventBus {
        void register(Consumer<EventObject> eventHandler);

        void unregister(Consumer<EventObject> eventHandler);
    }

    @Tag("div")
    public class UserNameLabel extends Component {

        @Override
        protected void onAttach() {
            // user name can be stored to session after login
            //@formatter:off - custom line wrapping
            String userName = (String) getUI().get().getSession().getAttribute("username");
            getElement().setTextContent("Hello " + userName + ", welcome back!");
            //@formatter:on
        }
    }

    @Tag("div")
    public class ShoppingCartSummaryLabel extends Component {

        @Override
        protected void onAttach() {
            //@formatter:off - custom line wrapping
            ShopEventBus eventBus = getUI().get().getSession().getAttribute(ShopEventBus.class);
            //@formatter:on
            // registering to event bus for updates from other components
            eventBus.register(this::onCartSummaryUpdate);
        }

        @Override
        protected void onDetach() {
            ShopEventBus eventBus = getUI().get().getSession()
                    .getAttribute(ShopEventBus.class);
            // after detaching don't need any updates
            eventBus.unregister(this::onCartSummaryUpdate);
        }

        private void onCartSummaryUpdate(EventObject event) {
            // update cart summary ...
        }
    }
}
