package japko6.workly.ui.base;

public class BasePresenter<T> {

    private T view = null;

    public void onLoad(T view) {
        this.view = view;
    }

    protected T getView() {
        if (view == null) {
            throw new IllegalStateException("Initialize presenter first");
        } else {
            return view;
        }
    }
}
