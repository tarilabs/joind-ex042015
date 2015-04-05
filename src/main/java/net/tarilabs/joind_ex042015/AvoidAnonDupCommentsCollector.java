package net.tarilabs.joind_ex042015;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class AvoidAnonDupCommentsCollector implements Collector<Comment, List<Comment>, List<Comment>> {

	@Override
	public BiConsumer<List<Comment>, Comment> accumulator() {
		return (list, comment) -> {
			if (comment.getRating() == 0) {
				return;
			}
			if (!comment.isAnon()) {
				list.add(comment);
				return;
			}
			if (comment.isAnon() && comment.isEmptyText()) {
				list.add(comment);
				return;
			}
			if (comment.isAnon() && !comment.isEmptyText()) {
				boolean alreadyFoundAnonText = list.stream().anyMatch(c -> comment.isAnon() && !c.isEmptyText() && c.getComment().equals(comment.getComment()) );
				if (!alreadyFoundAnonText) {
					list.add(comment);
				}
				return;
			}
			throw new RuntimeException("???");
		};
	}

	@Override
	public Set<java.util.stream.Collector.Characteristics> characteristics() {
		return Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH));
	}

	@Override
	public BinaryOperator<List<Comment>> combiner() {
		return (l1, l2) -> {
			l1.addAll(l2);
			return l1;
		};
	}

	@Override
	public Function<List<Comment>, List<Comment>> finisher() {
		return Function.identity();
	}

	@Override
	public Supplier<List<Comment>> supplier() {
		return ArrayList::new;
	}


}
