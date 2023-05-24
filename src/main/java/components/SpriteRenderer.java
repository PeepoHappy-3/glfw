package components;

import DreamEngine.Component;

public class SpriteRenderer extends Component {

    private boolean firstTime = false;
    @Override
    public void update(float dt) {
        if(!firstTime){
            System.out.println("im updating");
            firstTime = true;
        }

    }

    @Override
    public void start() {
        System.out.println("im starting");
    }
}
