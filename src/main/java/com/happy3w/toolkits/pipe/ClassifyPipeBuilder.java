package com.happy3w.toolkits.pipe;

public class ClassifyPipeBuilder<HeaderType, InType, TagType> {
    private final IEasyPipe<HeaderType> head;
    private final ClassifyPipe<InType, TagType> current;

    public ClassifyPipeBuilder(IEasyPipe<HeaderType> head, ClassifyPipe<InType, TagType> current) {
        this.head = head;
        this.current = current;
    }

    public ClassifyPipeBuilder<HeaderType, InType, TagType> pipe(TagType tag, IEasyPipe<InType> subPipe) {
        current.pipe(tag, subPipe);
        return this;
    }

    public EasyPipeBuilder<HeaderType, InType, InType> others() {
        HeadPipe<InType> newHead = new HeadPipe<>();
        current.setDefaultPipe(newHead);
        return new EasyPipeBuilder<>(head, newHead);
    }

    public IEasyPipe<HeaderType> build() {
        return head;
    }
}
