package com.luckyhan.rubychina.upload;

import com.squareup.tape.InMemoryObjectQueue;
import com.squareup.tape.ObjectQueue;
import com.squareup.tape.Task;
import com.squareup.tape.TaskQueue;

public class UploadTaskQueue extends TaskQueue<Task> {

    public static UploadTaskQueue create() {
        return new UploadTaskQueue(new InMemoryObjectQueue<Task>());
    }

    public UploadTaskQueue(ObjectQueue<Task> delegate) {
        super(delegate);
    }

    @Override
    public void add(Task entry) {
        super.add(entry);
    }

}
