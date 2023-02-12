export default function createEdgesFromList(obj) {
    let edges = []
    console.log(obj)
    obj.stops.forEach((s,id)=> {
        console.log(id)
        if(!s.isEnd || id<obj.stops.length-1) {
            edges.push(Object.assign({}, {
                    from: {lat: s.lat, lon: s.lon, name: s.name},
                to: {lat: obj.stops[id+1].lat, lon: obj.stops[id+1].lon, name:obj.stops[id+1].name},
                routeID: obj.id,
                routeName: obj.name,
                routeInterval: obj.timeInterval,
                machinists: obj.machinists.map( m=> { return m.name }),
                dispatchers: obj.dispatchers.map( d=> { return d.name }),
                color: obj.color,
                timeInterval: obj.timeInterval
            }))
        } else {
            edges.push(Object.assign({}, {
                from: {lat: s.lat, lon: s.lon, name: s.name},
                to: {lat: s.lat, lon: s.lon, name: s.name},
                routeID: obj.id,
                routeName: obj.name,
                routeInterval: obj.timeInterval,
                machinists: obj.machinists.map( m=> { return m.name }),
                dispatchers: obj.dispatchers.map( d=> { return d.name }),
                color: obj.color,
                timeInterval: obj.timeInterval
            }))
        }
    })
    return edges
}