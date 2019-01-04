class SmartTreeItem {

    static of(object) {
        const item = new SmartTreeItem(object.id);
        Object.assign(item, object);
        return item;
    }

    constructor(id) {
        this.id = id;
        this.parent = null;
        this.children = [];
    }

    addChild(item) {
        this.children.push(item);
        item.parent = this;
    }

    removeChild(item) {
        this.children.splice(this.children.indexOf(item), 1);
        item.parent = null;
    }

    find(id) {
        if (this.id === id) {
            return this;
        }
        for (let child of this.children) {
            const result = child.find(id);
            if (result) {
                return result;
            }
        }
    }
}

const world = new SmartTreeItem("WORLD");

const webSocket = new WebSocket('ws://localhost:8080/game');
webSocket.onmessage = msg => {
    console.log(msg.data);
    const changes = JSON.parse(msg.data);
    for (let change of changes) {
        const item = world.find(change.id);
        if (item) {
            const changeType = change.type;
            if (changeType === 'CHILDREN_ADD') {
                item.addChild(SmartTreeItem.of(change.value));
            }
            else if (changeType === 'CHILDREN_REMOVE') {
                item.removeChild(item.find(change.value.id));
            }
            else if (changeType === 'PROPERTY_CHANGE') {
                Object.assign(item, { [change.value.n]: change.value.v });
            }
        }
    }
    if (changes.length) {
        console.log(world);
    }
}